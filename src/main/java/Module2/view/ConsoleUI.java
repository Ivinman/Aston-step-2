package Module2.view;

import java.util.Scanner;

public class ConsoleUI {
	private final Scanner scanner;
	private final Runnable promptAction;

	public ConsoleUI(Scanner scanner, Runnable promptAction) {
		this.scanner = scanner;
		this.promptAction = promptAction;
	}

	public void print(String message) {
		System.out.println(message);
	}

	public String readLine() {
		return scanner.nextLine().trim();
	}

	public boolean confirm(String prompt) {
		print(prompt + " (y/n)");
		while (true) {
			String input = readLine().toLowerCase();
			switch (input) {
				case "y": return true;
				case "n": return false;
				case "-help": print("Введите 'y' для подтверждения или 'n' для отмены");
				case "-exit": {
					System.exit(0);
					return false;
				}
				default: print("Команда не распознана. Введите 'y' для подтверждения или 'n' для отмены");
			}
		}
	}

	public void backToMain() {
		promptAction.run();
	}
}
