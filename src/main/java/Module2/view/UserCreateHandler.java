package Module2.view;

import Module2.AppController;
import Module2.repository.User;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public class UserCreateHandler {
	@Setter
	private String storedInput;
	private final InputProvider inputProvider;
	private final AppController controller;

	public UserCreateHandler(InputProvider inputProvider, AppController controller) {
		this.inputProvider = inputProvider;
		this.controller = controller;
	}

	public void createUser() {
		storedInput = "";
		User user = new User();
		editUser(user, UserField.ALL);
	}

	private void editUser(User user, UserField field) {
		switch (field) {
			case ALL -> {
				setName(user);
				setBirthDate(user);
				setEMail(user);
				saveUser(controller -> controller.createUser(user));
			}
			case NAME -> {
				setName(user);
				editUser(user);
			}
			case BIRTH_DATE -> {
				setBirthDate(user);
				editUser(user);
			}
			case EMAIL -> {
				setEMail(user);
				editUser(user);
			}
			case DONE -> {
				saveUser(controller -> controller.updateUser(user));
				inputProvider.getCommand();
			}
		}
	}

	public void editUser(User user) {
		System.out.println("edit");

		if (getUserCommand()) {
			System.out.println("stock= " + storedInput);
			System.out.println(user);
			editUser(user, UserField.of(storedInput).get());
		} else {
			inputProvider.getCommand();
		}
	}

	public boolean getUserCommand() {
		inputProvider.checkInput();

		if (checkCommand()) {
			return UserField.of(storedInput).isPresent();
		}
		return false;
	}

	private void setName(User user) {
		System.out.println("Введите имя");
		inputProvider.checkInput();

		if (checkCommand()) {
			String name = storedInput;
			if (name.length() < 2 || name.length() > 255) {
				System.out.println("Длинна");
				setName(user);
			} else {
				user.setName(name);
			}
		}
	}

	private void setBirthDate(User user) {
		System.out.println("Введите дату рождения в формате yyyy-mm-dd");
		inputProvider.checkInput();

		if (checkCommand()) {
			String birthDate = storedInput;
			if (!birthDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
				System.out.println("Формат");
				setBirthDate(user);
			} else {
				user.setBirthDate(LocalDate.parse(birthDate));
			}
		}
	}

	private void setEMail(User user) {
		System.out.println("Введите почтовый адрес");
		inputProvider.checkInput();

		if (checkCommand()) {
			String email = storedInput;
			if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
				System.out.println("Почта");
				setEMail(user);
			} else {
				user.setEmail(email);
			}
		}
	}

	private void saveUser(Consumer<AppController> action) {
		if (!checkCommand()) {
			return;
		}

		if (inputProvider.getConfirm()) {
			action.accept(controller);
		}

		inputProvider.getCommand();
	}

	private boolean checkCommand() {
		if (storedInput.isEmpty() || storedInput.isBlank()) {
			System.out.println("Введите -abort, чтобы прервать создание пользователя");
			System.out.println("Введите -help user, чтобы увидеть все команды создания пользователя");
			System.out.println("Введите -help, чтобы увидеть все основные команды");
			inputProvider.checkInput();
			return false;
		} else if (storedInput.equalsIgnoreCase(UserField.ABORT.command)) {
			inputProvider.getCommand();
			return false;
		} else if (storedInput.equalsIgnoreCase(ConsoleCommand.Command.EXIT.getCommand())) {
			controller.exit();
			return false;
		} else if (storedInput.equalsIgnoreCase(ConsoleCommand.Command.HELP.getCommand())) {
			ConsoleCommand.Command.printCommands();
		} else if (storedInput.equalsIgnoreCase(UserField.USER_HELP.command)) {
			System.out.println("Помошь");
		}
		return true;
	}

	public enum UserField {
		ABORT("-abort"),
		USER_HELP("-help user"),
		DONE("-done"),
		NAME("-name"),
		BIRTH_DATE("-birth"),
		EMAIL("-email"),
		ALL("-new");

		private final String command;

		UserField(String command) {
			this.command = command;
		}

		private static UserField[] userCommands() {
			return new UserField[] {NAME, BIRTH_DATE, EMAIL, DONE};
		}

		private static Optional<UserField> of(String input) {
			return Arrays.stream(userCommands())
					.filter(userField -> userField.command.equalsIgnoreCase(input))
					.findFirst();
		}
	}
}
