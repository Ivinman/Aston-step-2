package Module2.repository;

import Module2.view.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 255, message = "Name must be 2–255 characters long")
	private String name;

	@Email
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "age", nullable = false)
	@Min(value = 0, message = "Age should be positive")
	@Max(value = 120, message = "Age cannot be more than 120")
	private int age;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@Setter(AccessLevel.NONE)
	private LocalDateTime createdAt;

	public User(UserDto dto) {
		this.name = dto.getName();
		this.age = dto.getAge();
		this.email = dto.getEmail();
	}

	public User(String name, int age, String email) {
		this.name = name;
		this.age = age;
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("%s%n  ID: %d%n  Полных лет: %d%n  Почта: %s%n",
				name, id, age, email);
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof User other)) return false;
		if (!other.canEqual(this)) return false;
		if (this.id != other.getId()) return false;
		final LocalDateTime this$createdAt = this.createdAt;
		final LocalDateTime other$createdAt = other.getCreatedAt();
		return this$createdAt.equals(other$createdAt);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof User;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $id = this.id;
		result = result * PRIME + Long.hashCode($id);
		final Object $createdAt = this.createdAt;
		result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
		return result;
	}
}
