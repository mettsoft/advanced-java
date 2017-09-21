package Utility;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;

public final class PromptBuilder {

	private Scanner scanner;
	private PrintStream printStream;

	public PromptBuilder(InputStream inputStream, PrintStream printStream) {
		this.scanner = new Scanner(inputStream);
		this.printStream = printStream;
	}

	public PromptBuilder prompt(String message) {
		printStream.print(message);
		return this;
	}

	public String getNextLine() {
		return scanner.nextLine();
	}

	public <R> R getNextLine(Function<String, R> transferFunction) {
		return transferFunction.apply(scanner.nextLine());
	}
}
