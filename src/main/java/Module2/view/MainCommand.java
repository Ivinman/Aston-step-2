package Module2.view;

import Module2.AppController;

import java.util.List;
import java.util.Optional;

public class MainCommand {
	private final ConsoleUI ui;
	private final AppController controller;

	public MainCommand(ConsoleUI ui, AppController controller) {
		this.ui = ui;
		this.controller = controller;
	}

	private boolean handleSpecialCommand(String input) {
		return Command.of(input).map(c -> {
			switch (c) {
				case EXIT -> {
					controller.exit();
					return false;
				}
				case HELP -> {
					ui.print(Command.printHelp());
					return false;
				}
				case USER_HELP -> {
					ui.print(UserCreateHandler.UserField.printHelp());
					return false;
				}
				default -> {
					return true;
				}
			}
		}).orElse(true);
	}

	private long getId() {
		ui.print("Укажите id пользователя");
		String id = ui.readLine();
		if (handleSpecialCommand(id)) {
			if (!id.matches("^\\d+$")) {
				ui.print("Число не распознано. " + Command.HELP.getTip());
				return -1;
			}
		}
		return Long.parseLong(id);
	}

	public void delete() {
		long id = getId();
		if (id > -1) {
			Optional<UserDto> userOptional = findUser();
			if (userOptional.isPresent()) {
				ui.print("Вы точно хотите удалить этого пользователя?");
				if (ui.confirm(ui.readLine())) {
					if (controller.deleteUser(id)) {
						ui.print("Пользователь с номером " + id + " удален");
					} else {
						ui.print("Пользователь с номером " + id + " не найден");
					}
				}
			}
		}
	}

	public void findUsers() {
		List<UserDto> users = controller.findAll();
		if (users.isEmpty()) {
			ui.print("База данных пуста");
		} else {
			ui.print(users.toString());
		}
	}

	public Optional<UserDto> findUser() {
		long id = getId();
		if (id > -1) {
			Optional<UserDto> userOptional = controller.findById(id);
			if (userOptional.isEmpty()) {
				ui.print("Пользователь с номером " + id + " не найден");
				return Optional.empty();
			} else {
				ui.print("Найден пользователь");
				ui.print(userOptional.get().toString());
				return userOptional;
			}
		}
		return Optional.empty();
	}

	public void exit() {
		if (ui.confirm("Вы точно хотите выйти?")) {
			controller.exit();
		}
	}
}
