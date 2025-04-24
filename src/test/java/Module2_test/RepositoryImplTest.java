package Module2_test;

import Module2.repository.RepositoryImpl;
import Module2.repository.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class RepositoryImplTest extends RepositoryImpl {
	private User user;

	@SuppressWarnings("resource")
	@Container
	private static final PostgreSQLContainer<?> postgres =
			new PostgreSQLContainer<>("postgres:17")
					.withDatabaseName("Prod")
					.withUsername("postgres")
					.withPassword("root");

	@BeforeAll
	void setUp() {
		System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
		System.setProperty("hibernate.connection.username", postgres.getUsername());
		System.setProperty("hibernate.connection.password", postgres.getPassword());
		System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
	}

	@Test
	void createUser_test_ok() {
		long expectedId = createUser(user);
		assertTrue(expectedId > 0);
	}

	@Test
	void updateUser_test_ok() {
		long expected = createUser(user);
		user.setName("Bob");
		long actual = updateUser(user);
		assertEquals(expected, actual);
	}

	@Test
	void getUserById_test_ok() {
		long id = createUser(user);
		Optional<User> actual = getUserById(id);
		assertTrue(actual.isPresent());
		assertEquals(actual.get(), user);
	}

	@Test
	void getUserById_test_fail() {
		Optional<User> actual = getUserById(1);
		assertTrue(actual.isEmpty());
	}

	@Test
	void getAllUsersById_test_ok() {
		User testUser = new User("Bob", LocalDate.of(2016, 1, 3), "mail@test.com");
		List<User> expected = List.of(user, testUser);

		createUser(user);
		createUser(testUser);

		List<User> actual = getUserById();
		assertTrue(expected.containsAll(actual));
		assertEquals(expected.size(), actual.size());
	}

	@Test
	void  getAllUsersById_test_fail() {
		List<User> actual = getUserById();
		assertTrue(actual.isEmpty());
	}

	@Test
	void deleteUser_test_ok() {
		long id = createUser(user);
		assertTrue(deleteUser(id));
	}

	@Test
	void deleteUser_test_fail() {
		assertFalse(deleteUser(1L));
	}

	@Test
	void nonUniqueEmail_test() {
		createUser(user);
		assertThrows(RuntimeException.class, () -> createUser(user));
	}

	@BeforeEach
	void clearAllData() {
		user = new User("Alice", LocalDate.of(2012, 1, 10), "mail@mail.com");

		jpa.run(manager ->
			manager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE")
					.executeUpdate());
	}
}
