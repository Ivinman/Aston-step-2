package Module2.utils;

public class DtoValidatorUtil {
	public static boolean validName(String name) {
		return name != null && name.length() >= 2 && name.length() <= 255;
	}

	public static boolean validAge(String age) {
		return age.matches("^\\d+$") && Integer.parseInt(age) >= 0;
	}

	public static boolean validEmail(String email) {
		return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}
}
