package Module2.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtils {
	private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = FACTORY.getValidator();

	public static <T> void validate(T object) {
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
		if (!violations.isEmpty()) {
			String errorMessages = violations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining("; "));
			throw new IllegalArgumentException("Validation error: " + errorMessages);
		}
	}
}
