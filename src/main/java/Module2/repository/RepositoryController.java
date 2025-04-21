package Module2.repository;

import java.util.List;
import java.util.Optional;

public interface RepositoryController {
	long deleteUser(long id);
	long updateUser(User user);
	List<User> findUser();
	Optional<User> findUser(long id);
	long newUser(User user);
	void exit();
}
