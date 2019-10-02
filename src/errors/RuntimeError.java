package errors;

import scanner.Token;

public class RuntimeError extends Exception {

	public RuntimeError(Token token, String message){
		super(message);
	}
}
