package Module2.view;

import Module2.AppController;
import lombok.Getter;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserCreateHandler {

	private final AtomicBoolean isEditing = new AtomicBoolean(true);
	private final AppController controller;
	private final ConsoleUI ui;
	private final UserFieldService service;

	private static final Map<UserField, BiConsumer<UserCreateHandler, UserDto>> editActions = new EnumMap<>(UserField.class);

	public UserCreateHandler(AppController controller, ConsoleUI ui) {
		this.controller = controller;
		this.ui = ui;
		this.service = new UserFieldService(ui, this);

		editActions.put(UserField.NAME, UserCreateHandler::setName);
		editActions.put(UserField.BIRTH_DATE, UserCreateHandler::setAge);
		editActions.put(UserField.EMAIL, UserCreateHandler::setEmail);
	}

	public void createUser() {
		setAllUserFields(new UserDto());
	}

	private void setAllUserFields(UserDto user) {
		setName(user);
		setAge(user);
		setEmail(user);
		confirmAndSave(() -> {
			long id = controller.createUser(user);
			if (id > -1) {
				ui.print("Пользователь успешно создан. Его номер:\n" + id);
			} else {
				ui.print("Произошла ошибка при создании нового пользователя");
				ui.print("Пожалуйста, попробуйте еще раз позднее");
			}
		});
	}

	public void editUser(UserDto user) {
		while (isEditing.get()) {
			ui.print("Укажите, какое поле хотите отредактировать:");
			String input = ui.readLine();
			if (handleSpecialCommand(input)) {
				UserField.of(input).ifPresentOrElse(
						field -> {
							if (field == UserField.DONE) {
								confirmAndSave(() -> {
									if (controller.updateUser(user)) {
										ui.print("Пользователь успешно обновлен:");
										ui.print(user.toString());
									} else {
										ui.print("Произошла ошибка при обновлении пользователя");
										ui.print("Пожалуйста, попробуйте еще раз позднее");
									}
								});
							} else {
								editActions.getOrDefault(field,
												(_, _) -> ui.print("Такого поля не существует"))
										.accept(this, user);
							}
						},
						() -> ui.print("Неизвестная команда. " + UserField.USER_HELP.getTip())
				);
			}
		}
	}

	private void setName(UserDto user) {
		String name = service.checkName();
		if (name != null) user.setName(name);
	}

	private void setAge(UserDto user) {
		user.setAge(service.checkAge());
	}

	private void setEmail(UserDto user) {
		String email = service.checkEmail();
		if (email != null) user.setEmail(email);
	}

	private void confirmAndSave(Runnable action) {
		if (ui.confirm("Сохранить изменения?")) {
			action.run();
		}
		ui.backToMain();
	}

	public boolean handleSpecialCommand(String input) {
		return UserField.of(input).map(field -> {
			switch (field) {
				case ABORT -> {
					isEditing.set(false);
					ui.backToMain();
					return false;
				}
				case EXIT -> {
					isEditing.set(false);
					controller.exit();
					return false;
				}
				case HELP -> {
					isEditing.set(false);
					ui.print(Command.printHelp());
					return false;
				}
				case USER_HELP -> {
					ui.print(UserField.printHelp());
					return false;
				}
				default -> {
					return true;
				}
			}
		}).orElse(true);
	}

	public enum UserField {
		HELP("-help", "Введите '-help' для общей справки"),
		USER_HELP("-help user", "Введите '-help user' для справки по созданию нового пользователя"),
		NAME("-name", "Введите '-name' для нового имени"),
		BIRTH_DATE("-birth", "Введите '-birth' для новой даты рождения"),
		EMAIL("-email", "Введите '-email' для нового почтового адреса"),
		ALL("-all", "Введите '-all' для последовательного редактирования всех полей"),
		ABORT("-abort", "Введите '-abort' для отмены"),
		DONE("-done", "Введите '-done' для завершения редактирования"),
		EXIT("-exit", "Введите '-exit' для выхода");

		private final String command;
		@Getter
		private final String tip;

		private static final Map<String, UserField> map = Arrays.stream(values())
				.collect(Collectors.toMap(cmd -> cmd.command.toLowerCase(), Function.identity()));

		UserField(String command, String tip) {
			this.command = command;
			this.tip = tip;
		}

		public static Optional<UserField> of(String input) {
			return Optional.ofNullable(map.get(input.toLowerCase()));
		}

		public static String printHelp() {
			return Arrays.stream(values())
					.map(f -> f.tip + "\n")
					.collect(Collectors.joining());
		}
	}
}