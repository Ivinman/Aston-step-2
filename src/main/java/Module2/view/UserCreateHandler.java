package Module2.view;

import Module2.AppController;
import Module2.repository.User;
import lombok.Setter;

import java.time.LocalDate;

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
		storedInput = "";
		switch (field) {
			case ALL -> {
				setName(user);
				setBirthDate(user);
				setEMail(user);
				saveUser(user);
			}
			case NAME -> setName(user);
			case BIRTH_DATE -> setBirthDate(user);
			case EMAIL -> {
				setEMail(user);
			}
			case DONE -> saveUser(user);
		}
	}

	public void editUser(User user) {
		storedInput = "";
		System.out.println("edit");
	}

	private void setName(User user) {
		System.out.println("Введите имя");
		inputProvider.checkInput();

		if (checkCommand(user)) {
			String name = storedInput;
			if (name.length() < 2 || name.length() > 255) {
				System.out.println("Длинна");
				setName(user);
			} else {
				user.setName(name);
			}
		}
	}

	private void setEMail(User user) {
		System.out.println("Введите почтовый адрес");
		inputProvider.checkInput();

		if (checkCommand(user)) {
			String email = storedInput;
			if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
				System.out.println("Почта");
				setEMail(user);
			} else {
				user.setEmail(email);
			}
		}
	}

	private void saveUser(User user) {
		controller.createUser(user);
		inputProvider.getCommand();
	}

	private boolean checkCommand(User user) {
		if (storedInput.isEmpty() || storedInput.isBlank()) {
			System.out.println("Введите -abort, чтобы прервать создание пользователя");
			System.out.println("Введите -help user, чтобы увидеть все команды создания пользователя");
			System.out.println("Введите -help, чтобы увидеть все основные команды");
			inputProvider.checkInput();
			return false;
		} else if (storedInput.equalsIgnoreCase(UserField.ABORT.command)) {
			user = null;
			inputProvider.getCommand();
			return false;
		} else if (storedInput.equalsIgnoreCase(ConsoleCommand.Command.EXIT.getCommand())) {
			controller.exit();
			return false;
		} else if (storedInput.equalsIgnoreCase(ConsoleCommand.Command.HELP.getCommand())) {
			user = null;
			ConsoleCommand.Command.printCommands();
		} else if (storedInput.equalsIgnoreCase(UserField.USER_HELP.command)) {
			user = null;
			System.out.println("Помошь");
//			TODO edit user
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
	}
}
