package Module2.view;

public class DtoValidator {
	public boolean validName(String name) {
		return name != null && name.length() >= 2 && name.length() <= 255;
	}

	public boolean validAge(String age) {
		return age.matches("^\\d+$") && Integer.parseInt(age) >= 0;
	}

	public boolean validEmail(String email) {
		return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}
}
