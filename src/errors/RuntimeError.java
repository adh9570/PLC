package errors;

import scanner.Token;

import static driver.Application.*;

public class RuntimeError extends Exception {

	public RuntimeError(Token token, String message){
		super(
				"Runtime Error: " +
						message +
						", \"" + SCANNER.getLine(token.getLine()) + "\" " +
						"(" + FILE_NAME + ":" + token.getLine() + ")"
		);
	}
}
