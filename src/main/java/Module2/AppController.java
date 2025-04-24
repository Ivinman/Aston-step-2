package Module2;

import Module2.service.UserService;
import Module2.repository.Repository;
import Module2.repository.RepositoryImpl;
import Module2.repository.User;
import Module2.view.ConsoleCommand;
import Module2.view.InputProvider;

import java.util.List;
import java.util.Optional;

public class AppController {
	private final UserService userService;

	public AppController() {
		Repository repository = new RepositoryImpl();
		this.userService = new UserService(repository);
//		new ConsoleCommand(this);
		new InputProvider(this);
	}

	public long createUser(User user) {
		return userService.createUser(user);
	}

	public long updateUser(User user) {
		return userService.updateUser(user);
	}

	public boolean deleteUser(long id) {
		return userService.deleteUser(id);
	}

	public List<User> getAllUsers() {
		return userService.findAll();
	}

	public Optional<User> getUserById(long id) {
		return userService.findById(id);
	}

	public void exit() {
		userService.exit();
		System.exit(0);
	}
}
