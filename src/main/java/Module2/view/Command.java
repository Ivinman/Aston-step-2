package Module2.view;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Command {
	HELP("-help", "Введите '-help' для общей справки"),
	USER_HELP("-help user", "Введите '-help user' для справки по созданию нового пользователя"),
	NEW_USER("-new", "Введите '-new' для создания нового пользователя"),
	EDIT("-edit", "Введите '-edit' для редактирования пользователя"),
	DELETE_USER("-delete", "Введите '-delete' для удаления пользователя"),
	FIND_ALL("-find all", "Введите '-find all' чтобы увидеть всех пользователей"),
	FIND_USER("-find", "Введите '-find' для поиска пользователя"),
	EXIT("-exit", "Введите '-exit' для выхода");

	private final String command;
	@Getter
	private final String tip;

	Command(String command, String tip) {
		this.command = command;
		this.tip = tip;
	}

	public static Optional<Command> of(String input) {
		return Arrays.stream(values())
				.filter(cmd -> cmd.command.equalsIgnoreCase(input))
				.findFirst();
	}

	public static String printHelp() {
		return Arrays.stream(values())
				.map(c -> c.tip + "\n")
				.collect(Collectors.joining());
	}
}
