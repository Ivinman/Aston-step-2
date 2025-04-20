package Module2;

public interface CommandInterface {
	void newUser();
	void editUser(User user);
	void deleteUser(String message);
	void findUsers();
	User findUser(String message);
}
