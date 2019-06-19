package Zip;


import java.io.*;
import java.util.zip.*;
import org.apache.commons.cli.*;


public class Bonus {
	static String input; //-in
	static String output; // -o
	boolean help; //-h
	static String include; //-i
	static String ignore; // -ic

	public static void main(String[] args) {

		input = args[1];
		output = args[3];

		if(args.length>=5) {
			if(args[4].equals("-i")) {
				include = args[5];
			}else if(args[4].equals("-ic")) {
				ignore = args[5];
			}
		} 


		try {
			creatZipFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run (String[] args) {
		Options options = createOptions();

		if(parseOptions(options, args))
			if (help){
				printHelp(options);
				return;
			}

	}
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args); 

			input = cmd.getOptionValue("in");
			output = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
			include = cmd.getOptionValue("i");
			ignore = cmd.getOptionValue("ic");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		return true;

	}


	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("in").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("input path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("output path")
				.required()
				.build());

		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());


		options.addOption(Option.builder("i").longOpt("include")
				.desc("make include zip file, choose only one using i or ic")
				.hasArg()
				.argName("include word")
				.required()
				.build());

		options.addOption(Option.builder("ic").longOpt("ignore")
				.desc("make all zip file except that including word, choose only one using i or ic")
				.hasArg()
				.argName("ignore word")
				.required()
				.build());


		return options;

	}


	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Bonus";
		String footer ="";
		formatter.printHelp("Bonus Project", header, options, footer, true);
	}   

	public static void creatZipFile() throws Exception {

		String path = input;
		File file = new File(path);
		String files[] = file.list(); 

		byte[] buf = new byte[1024];



		try {

			String outFilename = output; 
			ZipOutputStream outFile = new ZipOutputStream(new FileOutputStream(outFilename));

			if(include == null && ignore == null) {


				for (int i=0; i<files.length; i++) {

					FileInputStream in = new FileInputStream( path + "/" + files[i]);

					outFile.putNextEntry(new ZipEntry(files[i])); 
					int len;

					while ((len = in.read(buf)) > 0) {
						outFile.write(buf, 0, len);
					}

					outFile.closeEntry();
					in.close();
				}
			}
			else if(ignore != null) {
				for (int i=0; i<files.length; i++) {
					if(files[i].contains(ignore) != true) {

						FileInputStream in = new FileInputStream( path + "/" + files[i]);

						outFile.putNextEntry(new ZipEntry(files[i])); 
						int len;

						while ((len = in.read(buf)) > 0) {
							outFile.write(buf, 0, len);
						}

						outFile.closeEntry();
						in.close();
					}
				}
			}
			else if (include != null) {
				for (int i=0; i<files.length; i++) {
					if(files[i].contains(include)) {

						FileInputStream in = new FileInputStream( path + "/" + files[i]);

						// Add ZIP entry to output stream.
						outFile.putNextEntry(new ZipEntry(files[i])); // Zip 파일에 경로를 정하여 저장할수 있다.

						// Transfer bytes from the file to the ZIP file
						int len;
						while ((len = in.read(buf)) > 0) {

							outFile.write(buf, 0, len);
						}

						// Complete the entry
						outFile.closeEntry();
						in.close();
					}
				} 
			}
			outFile.close();
		}catch(Exception e) {

			throw e;
		}

	}
}

