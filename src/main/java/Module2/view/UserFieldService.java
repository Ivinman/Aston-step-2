package Module2.view;

public class UserFieldService {
	private final ConsoleUI ui;
	private final DtoValidator validator;
	private final UserCreateHandler createHandler;

	public UserFieldService(ConsoleUI ui, UserCreateHandler createHandler, DtoValidator validator) {
		this.ui = ui;
		this.validator = validator;
		this.createHandler = createHandler;
	}

	public String checkName() {
		while (true) {
			ui.print("Введите имя:");
			String input = ui.readLine();
			if (createHandler.handleSpecialCommand(input)) {
				if (validator.validName(input)) return input;
				ui.print("Имя должно быть от 2 до 255 символов");
			}
		}
	}

	public String checkEmail() {
		while (true) {
			ui.print("Введите email:");
			String input = ui.readLine();
			if (createHandler.handleSpecialCommand(input)) {
				if (validator.validEmail(input)) return input;
				ui.print("Неверный email");
			}
		}
	}

	public int checkAge() {
		while (true) {
			ui.print("Введите возраст:");
			String input = ui.readLine();
			if (createHandler.handleSpecialCommand(input)) {
				if (validator.validAge(input)) return Integer.parseInt(input);
				ui.print("Неверный формат даты");
			}
		}
	}
}
