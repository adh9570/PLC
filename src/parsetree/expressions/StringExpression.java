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
			obj = new Concat(tokenStream);
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


	class Concat implements GrammarValue{

		Object obj1;
		Object obj2;

		Concat(TokenStream tokenStream) throws SyntaxError {

			// First Parenthesis
			Token openParen = tokenStream.getNextToken();
			if( openParen.getType() != Token.Type.START_PAREN ){
				throw new SyntaxError(openParen, "Missing opening parenthesis for concat");
			}

			Token token1 = tokenStream.getNextToken();

			if( tokenStream.getNextToken().getType() != Token.Type.COMMA ){
				throw new SyntaxError(token1, "Missing parameter for concat");
			}
			Token token2 = tokenStream.getNextToken();

			if( token1.getType() == Token.Type.STRING ){
				obj1 = token1.getValue();
			}
			else if( token1.getType() == Token.Type.ID_OR_KEYWORD ){
				obj1 = getScope(token1.getValue());
			}
			else{
				throw new SyntaxError(token1, "Improper type for concat paramater");
			}

			if( token2.getType() == Token.Type.STRING ){
				obj2 = token2.getValue();
			}
			else if( token2.getType() == Token.Type.ID_OR_KEYWORD ){
				obj2 = getScope(token2.getValue());
			}
			else{
				throw new SyntaxError(token2, "Improper type for concat paramater");
			}

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
			String val1;
			String val2;;

			if(obj1 instanceof GrammarValue )
				val1 = ((GrammarValue) obj1).getValue().toString();
			else
				val1 = obj1.toString();


			if(obj2 instanceof GrammarValue )
				val2 = ((GrammarValue) obj2).getValue().toString();
			else
				val2 = obj2.toString();


			return val1 + val2;
		}
	}
}
