package Module2.repository;

import Module2.utils.ValidatorUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Setter
public class RepositoryImpl implements Repository {
	protected JPA jpa;

	public RepositoryImpl() {
		jpa = new JPA();
	}

	@Override
	public boolean deleteUser(long id) {
		try {
			return jpa.run(manager -> {
				Optional<User> user = Optional.ofNullable(manager.find(User.class, id));
				if (user.isEmpty()) {
					return false;
				} else {
					manager.remove(user.get());
					return true;
				}
			});
		} catch (JPAException e) {
			log.error("Error deleting user with id {}: {}", id, e.getMessage());
			return false;
		}
	}

	@Override
	public long updateUser(User user) {
		try {
			return jpa.run(manager -> manager.merge(user)).getId();
		} catch (JPAException e) {
			log.error("Transaction failed during update for user {}: {}", user.getId(), e.getMessage());
			throw new RuntimeException("Cant update user", e);
		}
	}

	@Override
	public List<User> getUserById() {
		try {
			return jpa.run(manager -> {
				var cb = manager.getCriteriaBuilder();
				var cr = cb.createQuery(User.class);
				var root = cr.from(User.class);
				cr.select(root);
				return manager.createQuery(cr).getResultList();
			});
		} catch (JPAException e) {
			log.error("Error retrieving all users: {}", e.getMessage());
			throw new RuntimeException("Cant retrieve users", e);
		}
	}

	@Override
	public Optional<User> getUserById(long id) {
		try {
			return jpa.run(manager -> Optional.ofNullable(manager.find(User.class, id)));
		} catch (JPAException e) {
			log.warn("User not found: {}", e.getMessage());
			return Optional.empty();
		}
	}

	@Override
	public long createUser(User user) {
		ValidatorUtils.validate(user);
		try {
			return jpa.run(manager -> {
				manager.persist(user);
				return user.getId();
			});
		} catch (JPAException e) {
			log.error("Transaction failed during user creation: {}", e.getMessage());
			throw new RuntimeException("Cant create new user", e);
		}
	}

	@Override
	public void exit() {
		jpa.exit();
	}
}
