package Module2.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {
	private String name;
	private int age;
	private String email;

	@Override
	public String toString() {
		return String.format("%n%s%n  Полных лет:  %d%n  Почта:      %s%n",
				name, age, email);
	}
}


