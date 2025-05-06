package exceptions;

public class IncorrectGameStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectGameStateException(String error) {
		super(error);
	}
}
