package Module2.view;

import Module2.AppController;
import Module2.utils.ValidatorUtils;
import Module2.repository.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleCommand implements CommandInterface {
	private final AppController controller;
	private final Scanner input;

	public ConsoleCommand(AppController controller) {
		this.controller = controller;
		this.input = new Scanner(System.in);
		start();
	}

	private String getString(String message) {
		System.out.println(message);
		return input.nextLine().trim();
	}

	private Command getCommand(String message) {
		System.out.println(message);
		String cmd = input.nextLine().trim();
		Optional<Command> command = Command.ofPrimary(cmd);
		return command.orElse(null);
	}

	private int checkId(String command) {
		int id;
		try {
			id = Integer.parseInt(command);
		} catch (NumberFormatException e) {
			input.reset();
			id = checkId(getString("Введите целое число"));
		}
		return id;
	}

	@Override
	public void deleteUser(String message) {
		int id = checkId(getString(message));
		controller.deleteUser(id);
	}

	@SuppressWarnings("InfiniteRecursion")
	private void executeCommand(Command command) {
		switch (command) {
			case HELP -> Command.printCommands();
			case NEW_USER -> newUser();
			case EDIT -> {
				User user = findUser("Введите id пользователя:");
				System.out.println("Вы собиратесь изменить пользователя\n" + user);
				editUser(user);
			}
			case DELETE_USER -> deleteUser("Укажите id пользователя:");
			case FIND_ALL -> findUsers();
			case FIND_USER -> findUser("Введите id пользователя:");
			case EXIT -> controller.exit();
			case null, default ->
				executeCommand(getCommand("Неизвестная команда\nВведите " + Command.HELP.command + " для списка команд"));
		}
		executeCommand(getCommand("Введите команду:"));
	}

	private void start() {
		executeCommand(getCommand("Добро пожаловать в\nUser service v 1.0\nВведите команду:"));
	}

	@Override
	public void newUser() {
		User user = new User();
		System.out.println("Вы создаете нового пользователя");
		userConstructor(user, Command.NAME);
		userConstructor(user, Command.BIRTH_DATE);
		userConstructor(user, Command.EMAIL);
		ValidatorUtils.validate(user);
		System.out.printf("Пользователь успешно создан\nid %d%n", controller.createUser(user));
	}

	@Override
	public void editUser(User user) {
		String cmd = getString("Какое поле вы хотите изменить?");
		Optional<Command> command = Command.ofUser(cmd);
		if (command.isEmpty()) {
			System.out.printf
					("Команда %s не распознана. Введите %s для списка команд%n", cmd, Command.USER_HELP.command);
			editUser(user);
		} else {
			if (command.get().equals(Command.USER_HELP)) {
				Command.printUserCommands();
			}
			userConstructor(user, command.get());
		}
		String newCmd = getString("Продолжить редактирование?\ny\\n");
		switch (newCmd.toLowerCase()) {
			case "y" -> editUser(user);
			case "n" -> controller.updateUser(user);
			default -> System.out.printf
					("Команда %s не распознана. Введите %s для списка команд%n", newCmd, Command.USER_HELP.command);
		}
	}

	private void userConstructor(User user, Command command) {
		switch (command) {
			case NAME -> {
				String name = getString("Укажите имя:");
				if (!name.matches("^[a-zA-Zа-яА-Я ]+$")) {
					System.err.println("Неверное имя");
					userConstructor(user, Command.NAME);
				} else {
					user.setName(name);
				}
			}
			case BIRTH_DATE -> {
				try {
					LocalDate date = LocalDate.parse(getString("Укажите дату рождения (yyyy-mm-dd):"));
					user.setBirthDate(date);
				} catch (DateTimeParseException e) {
					System.err.println("Неверный формат даты");
					userConstructor(user, Command.BIRTH_DATE);
				}
			}
			case EMAIL -> {
				String email = getString("Укажите email:");
				if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
					System.err.println("Неверный адрес электронной почты");
					userConstructor(user, Command.EMAIL);
				} else {
					user.setEmail(email);
				}
			}
			default -> System.out.println("Неизвестная команда " + command +
					" конструктора пользователя\nВведите " + Command.USER_HELP + " для списка команд пользователя");
		}
	}

	@Override
	public void findUsers() {
		List<User> list = controller.getAllUsers();
		if (list.isEmpty()) {
			System.out.println("База данных пуста");
		} else {
			System.out.printf("Зарегистрированные пользователи:%n%s%n",
					String.join("\n", list.stream().map(User::toString).toList()));
		}
	}

	@Override
	public User findUser(String message) {
		long id = checkId(getString(message));
		Optional<User> user = controller.getUserById(id);
		if (user.isEmpty()) {
			findUser("Пользователь с id " + id + " не найден\nВведите другой id");
		} else
			return user.get();
		return null;
	}

	@Getter
	public enum Command {
		HELP("-help", "показать справку"),
		NEW_USER("-new", "добавить нового пользователя"),
		EDIT("-edit", "редактировать пользователя"),
		DELETE_USER("-delete", "удалить пользователя"),
		FIND_ALL("-find all", "показать всех пользователей"),
		FIND_USER("-find", "найти пользователя"),
		EXIT("-exit", "выход"),

		USER_HELP("-help user", "справка по командам пользователя"),
		NAME("-name", "изменить имя пользователя"),
		BIRTH_DATE("-birth", "изменить дату рождения пользователя"),
		EMAIL("-email", "изменить электронную почту пользователя");

		private final String command;
		private final String description;

		Command(String command, String description) {
			this.command = command;
			this.description = description;
		}

		public static void printCommands() {
			for (Command c : values()) {
				if (!c.name().contains("_HELP")) {
					System.out.printf("%s -- %s%n", c.command, c.description);
				}
			}
		}

		public static Command[] primaryValues() {
			return new Command[]{HELP, NEW_USER, EDIT, DELETE_USER, FIND_ALL, FIND_USER, EXIT};
		}

		public static Command[] userValues() {
			return new Command[]{USER_HELP, NAME, BIRTH_DATE, EMAIL, EXIT};
		}

		public static void printUserCommands() {
			for (Command c : userValues()) {
				System.out.printf("%s -- %s%n", c.command, c.description);
			}
		}

		public static Optional<Command> ofUser(String input) {
			return Arrays.stream(userValues())
					.filter(cmd -> cmd.command.equalsIgnoreCase(input))
					.findFirst();
		}

		public static Optional<Command> ofPrimary(String input) {
			return Arrays.stream(primaryValues())
					.filter(cmd -> cmd.command.equalsIgnoreCase(input))
					.findFirst();
		}
	}
}
