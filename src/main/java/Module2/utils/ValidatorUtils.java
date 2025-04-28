package Module2.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ValidatorUtils {
	private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = FACTORY.getValidator();

	public static <T> void validate(T object) {
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
		if (!violations.isEmpty()) {
			String errorMessages = violations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining("; "));
			log.error("Validation error {}", errorMessages);
			throw new IllegalArgumentException("Validation error: " + errorMessages);
		}
	}
}
