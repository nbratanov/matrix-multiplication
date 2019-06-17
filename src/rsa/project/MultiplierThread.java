package rsa.project;

public class MultiplierThread extends Thread {

	private final Matrix left;
	private final Matrix right;
	private final int currentThreadNumber;
	private final int numberOfThreads;
	private long elapsedTime;

	public MultiplierThread(Matrix left, Matrix right, int currentThreadNumber, int numberOfThreads) {
		this.left = left;
		this.right = right;
		this.currentThreadNumber = currentThreadNumber;
		this.numberOfThreads = numberOfThreads;
	}

	public void run() {
		long startTime = System.currentTimeMillis();
		matrixMultiply();
		this.elapsedTime = System.currentTimeMillis() - startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void matrixMultiply() {
		for (int i = currentThreadNumber; i < left.getRows(); i += numberOfThreads) {
			for (int k = 0; k < left.getColumns(); k++) {
				for (int j = 0; j < right.getColumns(); j++) {
					MatrixMultiplier.result.getData()[i][j] += left.getData()[i][k] * right.getData()[k][j];
				}
			}
		}
	}
}
