package parsetree.expressions;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import parsetree.expressions.Double.DoubleExpression;
import parsetree.expressions.Integer.IntegerExpression;
import parsetree.expressions.String.StringExpression;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.*;

/**
 * Expression
 * Extends {@link GrammarObject}
 * Implements {@link GrammarValue}
 */
public class Expression extends GrammarObject implements GrammarValue {

	private GrammarValue child;


	/**
	 * Expression(GrammarObject, TokenStream)
	 *
	 * @param parent - Parent {@link GrammarObject}
	 * @param tokenStream - TokenStream at current index
	 * @throws SyntaxError - Any Syntax error that may occur
	 */
	public Expression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
		super(parent);

		Object token = tokenStream.peekNextToken();
		Token.Type type = ((Token)token).getType();


		switch (type) {
			case ADD_OP:
				token = tokenStream.peekNextToken(2);
			case NUMBER:
				if (((Token) token).getValue().contains(".")) {
					child = new DoubleExpression(this, tokenStream);
				} else {
					child = new IntegerExpression(this, tokenStream);
				}
				break;
			case STRING:
				child = new StringExpression(this, tokenStream);
				break;
			case ID_OR_KEYWORD:
				if(((Token) token).getValue().equals("concat") || ((Token) token).getValue().equals("charAt")){
					child = new StringExpression(this, tokenStream);
					return;
				}
				token = getScope(((Token) token).getValue());

				switch (((GrammarValue) token).getType()) {
					case INTEGER:
						child = new IntegerExpression(this, tokenStream);
						break;
					case DOUBLE:
						child = new DoubleExpression(this, tokenStream);
						break;
					case STRING:
						child = new StringExpression(this, tokenStream);
						break;
					default:
						child = null;
						break;
				}
				break;
			default:
				break;
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
		return child.getType();
	}

	/**
	 * getValue()
	 *
	 * @return value as the corresponding type
	 */
	@Override
	public Object getValue() throws RuntimeError {
		return child.getValue();
	}


}
