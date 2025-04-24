package Module2.view;

import Module2.AppController;
import Module2.repository.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import static Module2.view.ConsoleCommand.Command.*;

public class InputProvider {
	//	private final ConsoleCommand command;
	private final Scanner input;
	private final InputOutputCoordinator coordinator;
	private final UserCreateHandler createHandler;


	public InputProvider(/*ConsoleCommand command, */AppController controller) {
//		this.command = command;
		input = new Scanner(System.in);
		coordinator = new InputOutputCoordinator(this, controller);
		createHandler = new UserCreateHandler(this, controller);
		System.out.println("Привет");
		getCommand();
	}

	private boolean handleSpecialCommands(String cmd) {
		if (cmd.equals(HELP.getCommand())) {
			printCommands();
			return true;
		} else if (cmd.equals(EXIT.getCommand())) {
			coordinator.exit();
			return true;
		}
		return false;
	}


	public void checkInput() {
		String input = this.input.nextLine().trim();
		if (handleSpecialCommands(input)) {
			getCommand();
		} else {
			createHandler.setStoredInput(input);
		}
	}

	public boolean getConfirm() {
		System.out.println("Вы уверены?");
		String cmd = input.nextLine().trim();

		if (handleSpecialCommands(cmd)) {
			return false;
		}

		return switch (cmd.toLowerCase()) {
			case "y" -> true;
			case "n" -> false;
			default -> getConfirm();
		};
	}


	public int getId() {
		String cmd = input.nextLine().trim();

		if (handleSpecialCommands(cmd)) {
			System.out.println("Новая команда");
			getCommand();
			return -1;
		} else if (!cmd.matches("^\\d+$")) {
			System.out.println("Число");
			getCommand();
			return -1;
		}

		return Integer.parseInt(cmd);
	}


	public void getCommand() {
		System.out.println("Введите вашу команду");
		String cmd = input.nextLine().trim();
		if (!cmd.startsWith("-")) System.out.println("Не команда");
		switch (ofPrimary(cmd).get()) {
			case HELP -> printCommands();
			case NEW_USER -> createHandler.createUser();
			case EDIT -> {
				Optional<User> user = coordinator.findUser();
				if (user.isPresent()) {
					System.out.printf("Вы собиратесь изменить пользователя%n%s%n", user.get());
					createHandler.editUser(user.get());
					getCommand();
				}
			}
			case DELETE_USER -> coordinator.delete();
			case FIND_ALL -> coordinator.findUsers();
			case FIND_USER -> {
				coordinator.findUser().ifPresent(System.out::println);
				getCommand();
			}
			case EXIT -> coordinator.exit();
			default -> {
				System.out.println("Неизвестная команда\nВведите " + HELP.getCommand() + " для списка команд");
				getCommand();
			}
		}
	}
}
