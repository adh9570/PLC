package errors;

import scanner.Token;

public class SyntaxError extends Exception {

	/**
	 * Constructor
	 *
	 * @param token token that error occurred on
	 * @param error error that happened
	 */
	public SyntaxError(Token token, String error){
		super(
			"Syntax Error: " + error +"\n" +
				"\t Error at:" + token.getLine() +
				"'" + token.getValue() + "'"
		);
	}

}
