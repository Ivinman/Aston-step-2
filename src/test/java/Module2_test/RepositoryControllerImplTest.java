package Module2_test;

import Module2.repository.JPA;
import Module2.repository.JPAException;
import Module2.repository.RepositoryControllerImpl;
import Module2.repository.User;
import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class RepositoryControllerImplTest {

	private JPA jpaMock;
	private RepositoryControllerImpl controller;

	@BeforeEach
	void setUp() {
		jpaMock = Mockito.mock(JPA.class);
		controller = new RepositoryControllerImpl();
		controller.setJpa(jpaMock);
	}

	@Test
	void newUser_test() {
		User user = new User();
		user.setName("Alice");
		user.setBirthDate(LocalDate.of(1990, 1, 1));
		user.setEmail("alice@example.com");
		user.setId(1010);

		when(jpaMock.run(any(Function.class)))
				.thenAnswer(invocation -> {
					Function<EntityManager, Long> callback = invocation.getArgument(0);
					EntityManager emMock = mock(EntityManager.class);
					return callback.apply(emMock);
				});

		long id = controller.newUser(user);
		assertEquals(1010, id);
	}

	@Test
	void newUser_shouldThrowExceptionOnDuplicateEmail() {
		User user = new User();
		user.setName("Alice");
		user.setBirthDate(LocalDate.of(1990, 1, 1));
		user.setEmail("alice@example.com");
		user.setId(1L);

		when(jpaMock.run(any(Function.class)))
				.thenReturn(user.getId())
				.thenThrow(JPAException.class);

		long id = controller.newUser(user);
		assertEquals(1L, id);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.newUser(user));

		assertTrue(exception.getMessage().contains("Cant create new user"));
	}


	@Test
	void deleteUser_Test_ok() {
		User user = new User();
		user.setId(2000);

		when(jpaMock.run(any(Function.class))).thenReturn(Optional.of(user));

		long deletedId = controller.deleteUser(2000);
		assertEquals(2000, deletedId);
	}

	@Test
	void deleteUser_test_notFound() {
		when(jpaMock.run(any(Function.class))).thenReturn(Optional.empty());

		long result = controller.deleteUser(1234);
		assertEquals(-1L, result);
	}

	@Test
	void findUser_test_ok() {
		User user = new User();
		user.setId(5000);

		when(jpaMock.run(any(Function.class))).thenReturn(Optional.of(user));

		Optional<User> result = controller.findUser(5000);
		assertTrue(result.isPresent());
		assertEquals(5000, result.get().getId());
	}

	@Test
	void findAllUsers_test() {
		List<User> expected = List.of(
				new User("Alice", LocalDate.of(1990, 1, 1), "a@mail.com"),
				new User("Bob", LocalDate.of(1985, 6, 15), "b@mail.com")
		);

		when(jpaMock.run(any(Function.class))).thenReturn(expected);

		List<User> actual = controller.findUser();
		assertTrue(actual.containsAll(expected));
		assertEquals(2, actual.size());
	}

	@Test
	void updateUser_test() {
		User user = new User();
		user.setId(1010);

		when(jpaMock.run(any(Function.class))).thenReturn(user);

		long updatedId = controller.updateUser(user);
		assertEquals(1010, updatedId);
	}
}
