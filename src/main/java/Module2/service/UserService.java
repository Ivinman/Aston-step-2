package Module2.service;

import Module2.repository.RepositoryImpl;
import Module2.repository.User;
import Module2.utils.ValidatorUtils;

import java.util.List;
import java.util.Optional;

public class UserService {
	private final RepositoryImpl repository;

	public UserService(RepositoryImpl repository) {
		this.repository = repository;
	}

	public long createUser(User user) {
		ValidatorUtils.validate(user);
		return repository.createUser(user);
	}

	public boolean updateUser(User user) {
		ValidatorUtils.validate(user);
		return repository.updateUser(user);
	}

	public boolean deleteUser(long id) {
		return repository.deleteUser(id);
	}

	public List<User> findAll() {
		return repository.getAllUsers();
	}

	public Optional<User> findById(long id) {
		return repository.getUserById(id);
	}

	public void exit() {
		repository.exit();
	}
}