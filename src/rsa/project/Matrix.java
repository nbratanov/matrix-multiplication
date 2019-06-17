package rsa.project;

import java.util.concurrent.ThreadLocalRandom;

public class Matrix {

	private final int rows;
	private final int columns;
	private final double data[][];

	public Matrix(int rows, int columns, boolean isRandomGenerated) {
		this.rows = rows;
		this.columns = columns;
		data = new double[rows][columns];

		if (isRandomGenerated == true) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					data[i][j] = ThreadLocalRandom.current().nextDouble(1, 100);
				}
			}
		}
	}

	public int getRows() {
		return this.rows;
	}

	public int getColumns() {
		return this.columns;
	}

	public double[][] getData() {
		return this.data;
	}

	public void print() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
