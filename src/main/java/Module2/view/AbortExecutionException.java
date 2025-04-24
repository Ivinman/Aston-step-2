package Module2.view;

public class AbortExecutionException extends RuntimeException {
	public AbortExecutionException() {
		super("Пользователь прервал выполнение");
	}
}
