package Module2.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private long id;
	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 255, message = "Name must be 2–255 characters long")
	private String name;
	@NotNull(message = "BirthDate cannot be null")
	@PastOrPresent(message = "BirthDate cannot be in future")
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

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof User other)) return false;
		if (!other.canEqual(this)) return false;
		if (this.getId() != other.getId()) return false;
		final LocalDateTime this$createdAt = this.getCreatedAt();
		final LocalDateTime other$createdAt = other.getCreatedAt();
		return this$createdAt.equals(other$createdAt);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof User;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $id = this.getId();
		result = result * PRIME + Long.hashCode($id);
		final Object $createdAt = this.getCreatedAt();
		result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
		return result;
	}
}
