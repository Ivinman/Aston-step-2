package Module2;

import Module2.service.UserService;
import Module2.repository.Repository;
import Module2.repository.RepositoryImpl;
import Module2.repository.User;
import Module2.utils.ValidatorUtils;
import Module2.view.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppController {
	private final UserService userService;
	private final ConsoleCommander commander;

	public AppController() {
		Repository repository = new RepositoryImpl();
		this.userService = new UserService(repository);
		Scanner scanner = new Scanner(System.in);
		ConsoleUI ui = new ConsoleUI(scanner, this::run);
		commander = new ConsoleCommander(ui,
				new UserCreateHandler(this, ui),
				new MainCommand(ui, this)
		);
		run();
	}

	public long createUser(UserDto userDto) {
		return userService.createUser(new User(userDto));
	}

	public boolean updateUser(UserDto userDto) {
		return userService.updateUser(new User(userDto));
	}

	public boolean deleteUser(long id) {
		return userService.deleteUser(id);
	}

	public List<UserDto> findAll() {
		return userService.findAll()
				.stream()
				.map(u -> new UserDto(u.getName(), u.getAge(), u.getEmail()))
				.toList();
	}

	public Optional<UserDto> findById(long id) {
		Optional<User> userOptional = userService.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return Optional.of(new UserDto(user.getName(), user.getAge(), user.getEmail()));
		}
		return Optional.empty();
	}

	private void run() {
		commander.run();
	}

	public void exit() {
		userService.exit();
		System.exit(0);
	}
}
