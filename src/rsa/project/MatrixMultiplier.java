package rsa.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixMultiplier {

	private int numberOfThreads;
	private Matrix left;
	private Matrix right;
	private boolean isQuiet;

	public static Matrix result;

	public MatrixMultiplier(int leftMatrixRows, int leftMatrixColumns, int rightMatrixColumns, boolean isQuiet) {

		this.left = new Matrix(leftMatrixRows, leftMatrixColumns, true);
		this.right = new Matrix(leftMatrixColumns, rightMatrixColumns, true);
		this.isQuiet = isQuiet;

		result = new Matrix(leftMatrixRows, rightMatrixColumns, false);
	}

	public MatrixMultiplier(String inputFile, boolean isQuiet) {

		initializeMatricesFromFile(inputFile);
		this.isQuiet = isQuiet;

		result = new Matrix(left.getRows(), right.getColumns(), false);
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public Matrix getLeftMatrix() {
		return left;
	}

	public Matrix getRightMatrix() {
		return right;
	}

	public void printResult() {
		result.print();
	}

	public void initializeMatricesFromFile(String inputFile) {

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String firstLine = br.readLine();
			if (firstLine == null) {
				System.out.println("The file is empty!");
				return;
			}

			String[] dimensions = firstLine.split(" ");
			left = new Matrix(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]), false);
			right = new Matrix(Integer.parseInt(dimensions[1]), Integer.parseInt(dimensions[2]), false);

			int counterToEndOfFirstMatrix = Integer.parseInt(dimensions[1]);
			String line = null;

			int row = 0;
			while ((row != (counterToEndOfFirstMatrix - 1)) && ((line = br.readLine()) != null)) {
				String[] digits = line.split(" ");
				for (int j = 0; j < digits.length; j++) {
					left.getData()[row][j] = Double.parseDouble(digits[j]);
				}
				row++;
			}

			row = 0;
			while ((line = br.readLine()) != null) {
				String[] digits = line.split(" ");
				for (int j = 0; j < digits.length; j++) {
					right.getData()[row][j] = Double.parseDouble(digits[j]);
				}
				row++;
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}

	public void saveResultToFile(String outputFile) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
			for (int i = 0; i < result.getData().length; i++) {
				for (int j = 0; j < result.getData()[i].length; j++) {
					bw.write(result.getData()[i][j] + ((j == result.getData()[i].length - 1) ? "" : " "));
				}
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void multiThreadedMultiply(int numberOfThreads) throws InterruptedException {

		long startTime = System.currentTimeMillis();

		MultiplierThread[] threads = new MultiplierThread[numberOfThreads];

		for (int i = 0; i < numberOfThreads; i++) {
			threads[i] = new MultiplierThread(left, right, i, numberOfThreads);
			threads[i].start();
			if (!isQuiet) {
				System.out.println("Thread-<" + i + "> started.");
			}
		}

		for (int i = 0; i < numberOfThreads; i++) {
			threads[i].join();
			if (!isQuiet) {
				System.out.println("Thread-<" + i + "> stopped.");
				System.out.println(
						"Thread-<" + i + "> execution time was (millis): " + threads[i].getElapsedTime());
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time for current run (millis): " + (endTime - startTime));
	}
}
