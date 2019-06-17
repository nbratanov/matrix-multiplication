package rsa.project;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	private static int m;
	private static int n;
	private static int k;
	private static int threads;
	private static String inputFile;
	private static String outputFile;
	private static boolean isQuiet;
	private static MatrixMultiplier multiplier;

	public static void main(String[] args) {

		Options options = new Options();
		options.addOption("m", true, "rows of left matrix");
		options.addOption("n", true, "common dimension of left and right matrix");
		options.addOption("k", true, "columns of right matrix");
		options.addOption("i", true, "file with input matrices");
		options.addOption("o", true, "file to save result matrix");
		options.addOption("t", "tasks", true, "number of threads");
		options.addOption("q", false, "quiet");

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;

		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		if (commandLine.hasOption("m") && commandLine.hasOption("n") && commandLine.hasOption("k")) {
			m = Integer.parseInt(commandLine.getOptionValue("m"));
			n = Integer.parseInt(commandLine.getOptionValue("n"));
			k = Integer.parseInt(commandLine.getOptionValue("k"));

		} else if (commandLine.hasOption("i")) {
			inputFile = commandLine.getOptionValue("i");
		}

		if (commandLine.hasOption("t") || commandLine.hasOption("tasks")) {
			threads = Integer.parseInt(commandLine.getOptionValue("t"));
		}

		if (commandLine.hasOption("q")) {
			isQuiet = true;
		} else {
			isQuiet = false;
		}

		if (inputFile == null) {
			multiplier = new MatrixMultiplier(m, n, k, isQuiet);
		} else {
			multiplier = new MatrixMultiplier(inputFile, isQuiet);
		}

		try {
			multiplier.multiThreadedMultiply(threads);

		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		if (commandLine.hasOption("o")) {
			outputFile = commandLine.getOptionValue("o");
			multiplier.saveResultToFile(outputFile);
		}
	}
}
