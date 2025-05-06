package server.exceptions;

public class NotEnoughFieldsException extends GenericExampleException{

	public NotEnoughFieldsException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
