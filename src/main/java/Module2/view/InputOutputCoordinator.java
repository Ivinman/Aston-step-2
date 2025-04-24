package Module2.view;

import Module2.AppController;
import Module2.repository.User;

import java.util.List;
import java.util.Optional;

public class InputOutputCoordinator {
	private final InputProvider inputProvider;
	private final AppController controller;

	public InputOutputCoordinator(InputProvider inputProvider, AppController controller) {
		this.inputProvider = inputProvider;
		this.controller = controller;
	}

	public void delete() {
		System.out.println("Введите номер пользователя");
		int id = inputProvider.getId();
		if (id > -1) {
			delete(id);
			inputProvider.getCommand();
		}
	}

	public void delete(int id) {
		if (inputProvider.getConfirm()) {
			if (controller.deleteUser(id)) {
				System.out.printf("Пользователь с номером %d удален%n", id);
			} else {
				System.out.printf("Пользователь с номером %d не найден", id);
				inputProvider.getCommand();
			}
		} else {
			inputProvider.getCommand();
		}
	}

	public void findUsers() {
		List<User> users = controller.getAllUsers();
		if (users.isEmpty()) {
			System.out.println("База данных пуста");
		} else {
			System.out.println(users);
		}
		inputProvider.getCommand();
	}

	public Optional<User> findUser() {
		System.out.println("Введите номер пользователя");
		int id = inputProvider.getId();
		if (id > -1) {
			Optional<User> user = controller.getUserById(id);
			if (user.isEmpty()) {
				System.out.printf("Пользователь с номером %s не найден%n", id);
				inputProvider.getCommand();
				return Optional.empty();
			} else {
				return user;
			}
		}
		return Optional.empty();
	}

	public void exit() {
		if (inputProvider.getConfirm()) {
			controller.exit();
		} else {
			inputProvider.getCommand();
		}
	}
}
