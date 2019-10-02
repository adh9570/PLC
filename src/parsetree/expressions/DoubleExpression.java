package parsetree.expressions;

import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.*;

class DoubleExpression extends GrammarObject implements GrammarValue{

	private Object obj1;
	private Token operation;
	private Object obj2;

	/**
	 * DoubleExpression(GrammarObject, TokenStream)
	 *
	 * @param parent - Parent {@link GrammarObject}
	 * @param tokenStream - TokenStream at current index
	 * @throws SyntaxError - Any Syntax error that may occur
	 */
	DoubleExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
		super(parent);

		Token token = tokenStream.getNextToken();
		if( token.getType() == Token.Type.NUMBER ){
			obj1 = token.getValue();
		}
		else{
			obj1 = getScope(token.getValue());
		}

		if( tokenStream.peekNextToken().getType() == Token.Type.MATH_OP){
			operation = tokenStream.getNextToken();
			token = tokenStream.getNextToken();

			if( token.getType() == Token.Type.NUMBER ){
				if( !token.getValue().contains(".") )
					throw new SyntaxError(token,
						"Type mismatch. Attempted to operate on a Double and Integer");
				obj2 = token.getValue();
			}
			else if(token.getType() == Token.Type.STRING ){
				throw new SyntaxError(token,
					"Type mismatch. Attempted to operate on a Double and String");
			}
			else{
				obj2 = getScope(token.getValue());
			}
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
		return DOUBLE;
	}

	/**
	 * getValue()
	 *
	 * @return value as the corresponding type
	 */
	@SuppressWarnings("Duplicates")
	@Override
	public Object getValue() {

		if( operation == null ){
			if ( obj1 instanceof GrammarValue )
				return ((GrammarValue) obj1).getValue().toString();
			return obj1.toString();
		}

		double val1;
		double val2;
		if ( obj1 instanceof GrammarValue )
			val1 = Double.parseDouble(((GrammarValue) obj1).getValue().toString());
		else
			val1 = Double.parseDouble(obj1.toString());
		if ( obj2 instanceof GrammarValue )
			val2 = Double.parseDouble(((GrammarValue) obj2).getValue().toString());
		else
			val2 = Double.parseDouble(obj2.toString());


		switch (operation.getValue()){
			case PLUS_OP:
				return val1 + val2;
			case MINUS_OP:
				return val1 - val2;
			case MULT_OP:
				return val1 * val2;
			case DIV_OP:
				return val1 / val2;
			default:
				return 0;
		}
	}

}
