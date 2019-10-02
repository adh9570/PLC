package parsetree.expressions;

import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.STRING;

class StringExpression extends GrammarObject implements GrammarValue{

	private Object obj;

	/**
	 * Expression(GrammarObject, TokenStream)
	 *
	 * @param parent - Parent {@link GrammarObject}
	 * @param tokenStream - TokenStream at current index
	 */
	StringExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
		super(parent);
		Token token = tokenStream.getNextToken();

		if( token.getType() == Token.Type.STRING ){
			obj = token.getValue();
		}
		else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("concat")) {
			obj = new Concat(this, tokenStream);
		}
		else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("charAt")){
			obj = new CharAt(this, tokenStream);
		}
		else{
			obj = getScope(token.getValue());
		}
	}



	/**
	 * getIdentifier()
	 *
	 * @return null
	 */
	@Override
	public String getIdentifier() {
		return null;
	}


	/**
	 * getType()
	 * @return type - String
	 */
	@Override
	public String getType() {
		return STRING;
	}

	/**
	 * getValue()
	 *
	 * @return value as the corresponding type
	 */
	@Override
	public Object getValue() {
		if( obj instanceof GrammarValue ){
			return ((GrammarValue)obj).getValue();
		}
		return obj;
	}


}

class Concat implements GrammarValue{

	private GrammarValue obj1;
	private GrammarValue obj2;

	Concat(StringExpression parent, TokenStream tokenStream) throws SyntaxError {

		// First Parenthesis
		Token openParen = tokenStream.getNextToken();
		if( openParen.getType() != Token.Type.START_PAREN ){
			throw new SyntaxError(openParen, "Missing opening parenthesis for concat");
		}

		obj1 = new StringExpression(parent, tokenStream);

		Token comma = tokenStream.getNextToken();
		if( comma.getType() != Token.Type.COMMA ){
			throw new SyntaxError(comma, "Missing parameter for concat");
		}

		obj2 = new StringExpression(parent, tokenStream);

		// Second Parenthesis
		Token closeParen = tokenStream.getNextToken();
		if( closeParen.getType() != Token.Type.END_PAREN ){
			throw new SyntaxError(closeParen, "Missing closing parenthesis for concat");
		}
	}


	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public String getType() {
		return STRING;
	}

	@Override
	public Object getValue() {
		String val1 = obj1.getValue().toString();
		String val2 = obj2.getValue().toString();

		return val1 + val2;
	}
}


class CharAt implements GrammarValue{

	private GrammarValue obj1;
	private GrammarValue obj2;

	CharAt(StringExpression parent, TokenStream tokenStream) throws SyntaxError {

		// First Parenthesis
		Token openParen = tokenStream.getNextToken();
		if( openParen.getType() != Token.Type.START_PAREN ){
			throw new SyntaxError(openParen, "Missing opening parenthesis for charAt");
		}

		obj1 = new StringExpression(parent, tokenStream);

		Token comma = tokenStream.getNextToken();
		if( comma.getType() != Token.Type.COMMA ){
			throw new SyntaxError(comma, "Missing parameter for charAt");
		}

		obj2 = new IntegerExpression(parent, tokenStream);

		// Second Parenthesis
		Token closeParen = tokenStream.getNextToken();
		if( closeParen.getType() != Token.Type.END_PAREN ){
			throw new SyntaxError(closeParen, "Missing closing parenthesis for charAt");
		}
	}


	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public String getType() {
		return STRING;
	}

	@Override
	public Object getValue() {
		String val1 = obj1.getValue().toString();
		int val2 = Integer.parseInt(obj2.getValue().toString());

		return val1.charAt(val2);
	}
}