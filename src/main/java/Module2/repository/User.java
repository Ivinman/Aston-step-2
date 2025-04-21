package Module2.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotBlank(message = "Name cannot be empty")
	private String name;
	@NotNull(message = "BirthDate cannot be null")
	private LocalDate birthDate;
	@Email(message = "Wrong format email")
	@Column(unique = true)
	private String email;
	@Setter(AccessLevel.NONE)
	@Min(value = 0, message = "Age should be positive")
	private int age;
	@Setter(AccessLevel.NONE)
	private LocalDateTime createdAt = LocalDateTime.now();

	public User(String name, LocalDate birth, String email) {
		this.name = name;
		setBirthDate(birth);
		this.email = email;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
		age = Period.between(birthDate, LocalDate.now()).getYears();
	}

	@Override
	public String toString() {
		return String.format("%s%n  ID: %d%n  Дата рождения: %s%n  Возраст: %d%n  Почта: %s%n",
				name, id, birthDate, age, email);
	}
}
