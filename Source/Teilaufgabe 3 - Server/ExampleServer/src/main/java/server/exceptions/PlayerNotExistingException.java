package server.exceptions;

public class PlayerNotExistingException extends GenericExampleException{
	
	public PlayerNotExistingException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
		
	}
}
