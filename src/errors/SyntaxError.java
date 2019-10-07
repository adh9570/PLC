package errors;

import scanner.Token;

import static driver.Application.*;

public class SyntaxError extends Exception {

	/**
	 * Constructor
	 *
	 * @param token token that error occurred on
	 * @param error error that happened
	 */
	public SyntaxError(Token token, String error){
		super(
			"Syntax Error: " + error +
					", \"" + SCANNER.getLine(token.getLine()) + "\" " +
					"(" + FILE_NAME + ":" + token.getLine() + ")"
		);
	}

}
