package Module2.view;

import Module2.repository.User;

public interface CommandInterface {
	void newUser();
	void editUser(User user);
	void deleteUser(String message);
	void findUsers();
	User findUser(String message);
}
