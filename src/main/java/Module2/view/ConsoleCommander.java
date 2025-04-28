package Module2.view;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleCommander {
	private final ConsoleUI ui;
	private final Map<Command, Runnable> commandMap = new EnumMap<>(Command.class);

	public ConsoleCommander(ConsoleUI ui, UserCreateHandler userHandler, MainCommand command) {
		this.ui = ui;

		commandMap.put(Command.HELP, () -> ui.print(Command.printHelp()));
		commandMap.put(Command.USER_HELP, () -> ui.print(UserCreateHandler.UserField.printHelp()));
		commandMap.put(Command.NEW_USER, userHandler::createUser);
		commandMap.put(Command.EDIT, () -> command.findUser().ifPresent(userHandler::editUser));
		commandMap.put(Command.DELETE_USER, command::delete);
		commandMap.put(Command.FIND_USER, command::findUser);
		commandMap.put(Command.FIND_ALL, command::findUsers);
		commandMap.put(Command.EXIT, command::exit);
	}

	public void run() {
		AtomicBoolean isRunning = new AtomicBoolean(true);
		while (isRunning.get()) {
			ui.print("Введите вашу команду:");
			Optional<Command> cmd = Command.of(ui.readLine());
			cmd.ifPresentOrElse(command -> {
						if (command == Command.EXIT) {
							ui.print("Завершение работы...");
							isRunning.set(false);
						}
						commandMap.get(command).run();
					},
					() -> ui.print("Неизвестная команда. " + Command.HELP.getTip())
			);
		}
	}
}
