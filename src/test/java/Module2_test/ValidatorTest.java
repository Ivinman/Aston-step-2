package Module2_test;

import Module2.repository.User;
import Module2.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
	User user;

	@BeforeEach
	void setUpUser() {
		user = new User();
		user.setId(5);
		user.setName("Barbara");
		user.setBirthDate(LocalDate.of(2012, 5, 8));
		user.setEmail("mail@mail.com");
	}

	@Test
	void validator_test_empty() {
		User user = new User();

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validate(user));

		String expected = "Validation error: ";
		String actual = exception.getMessage();

		assertTrue(actual.contains(expected));
	}

	@Test
	void validator_test_emptyName() {
		user.setName("");

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validate(user));

		String expected = "Validation error: ";
		String actual = exception.getMessage();

		assertTrue(actual.contains(expected));
	}

	@Test
	void validator_test_email() {
		user.setEmail("mail.ru");

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validate(user));

		String expected = "Validation error: ";
		String actual = exception.getMessage();

		assertTrue(actual.contains(expected));
	}

	@Test
	void validator_test_birthDate_Age() {
		user.setBirthDate(LocalDate.MAX);

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validate(user));

		String expected = "Validation error: ";
		String actual = exception.getMessage();

		assertTrue(actual.contains(expected));
	}

	@Test
	void validator_test_ok() {
		assertDoesNotThrow(() -> ValidatorUtils.validate(user));
	}
}
