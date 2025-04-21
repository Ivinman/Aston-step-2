package Module2;

import Module2.repository.RepositoryController;
import Module2.repository.RepositoryControllerImpl;
import Module2.repository.User;
import Module2.view.ConsoleCommand;

import java.util.List;
import java.util.Optional;

public class AppController {
	private final RepositoryController controller;

	public AppController() {
		controller = new RepositoryControllerImpl();
		new ConsoleCommand(this);
	}

	public void deleteUser(long id) {
		if (controller.deleteUser(id) == -1) {
			System.err.printf("Пользователь c id %d не найден\n", id);
		}
	}

	public void updateUser(User user) {
		try {
			System.out.printf("Данные пользователя с id %d обновлены\n", controller.updateUser(user));
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public List<User> findUser() {
		return controller.findUser();
	}

	public Optional<User> findUser(long id) {
		return controller.findUser(id);
	}

	public long newUser(User user) {
		return controller.newUser(user);
	}

	public void exit() {
		controller.exit();
		System.exit(0);
	}
}
