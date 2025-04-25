package Module2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDto(

        @NotBlank(message = "Name cannot be empty")
        @Size(min = 2, max = 255, message = "Name must be 2–255 characters long")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я ]+$")
        String name,

        @Email(message = "Wrong format email")
        String email,

        @Min(value = 0, message = "Age should be positive")
        Integer age
) {
}
