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
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class RepositoryWithContTest {
	private User user;
	private RepositoryImpl repository;

	//Оставил создание контейнера здесь, т.к. отдельный класс создавать в папке с тестами вроде не очень хорошо
	@SuppressWarnings("resource")
	@Container
	private static final PostgreSQLContainer<?> postgres =
			new PostgreSQLContainer<>("postgres:17")
					.withDatabaseName("Prod")
					.withUsername("postgres")
					.withPassword("root");

	@BeforeAll
	void starWithProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		properties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
		properties.setProperty("hibernate.connection.username", postgres.getUsername());
		properties.setProperty("hibernate.connection.password", postgres.getPassword());
		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		repository = new RepositoryImpl(properties);
	}

	@Test
	void createUser_test_ok() {
		long expectedId = repository.createUser(user);
		assertTrue(expectedId > 0);
	}

	@Test
	void updateUser_test_ok() {
		long expected = repository.createUser(user);
		user.setName("Bob");
		long actual = repository.updateUser(user);
		assertEquals(expected, actual);
	}

	@Test
	void getUserById_test_ok() {
		long id = repository.createUser(user);
		Optional<User> actual = repository.getUserById(id);
		assertTrue(actual.isPresent());
		assertEquals(actual.get(), user);
	}

	@Test
	void getUserById_test_fail() {
		Optional<User> actual = repository.getUserById(1);
		assertTrue(actual.isEmpty());
	}

	@Test
	void getAllUsersById_test_ok() {
		User testUser = new User("Bob", LocalDate.of(2016, 1, 3), "mail@test.com");
		List<User> expected = List.of(user, testUser);

		repository.createUser(user);
		repository.createUser(testUser);

		List<User> actual = repository.getUserById();
		assertTrue(expected.containsAll(actual));
		assertEquals(expected.size(), actual.size());
	}

	@Test
	void  getAllUsersById_test_fail() {
		List<User> actual = repository.getUserById();
		assertTrue(actual.isEmpty());
	}

	@Test
	void deleteUser_test_ok() {
		long id = repository.createUser(user);
		assertTrue(repository.deleteUser(id));
	}

	@Test
	void deleteUser_test_fail() {
		assertFalse(repository.deleteUser(1L));
	}

	@Test
	void nonUniqueEmail_test() {
		repository.createUser(user);
		assertThrows(RuntimeException.class, () -> repository.createUser(user));
	}

	@BeforeEach
	void clearAllData() {
		user = new User("Alice", LocalDate.of(2012, 1, 10), "mail@mail.com");

		repository.getJpa().run(manager ->
			manager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE")
					.executeUpdate());
	}
}
