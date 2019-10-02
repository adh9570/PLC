package parsetree;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.expressions.Expression;
import scanner.Token;
import scanner.TokenStream;

class Assignment extends GrammarObject implements GrammarValue{

	private final Token id;
	private final Token type;
	private Expression value;


	Assignment(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
		super(parent);
		tokenStream.subtractFromIndex(1);

		type = tokenStream.getNextToken();
		id = tokenStream.getNextToken();

		if( id.getType() != Token.Type.ID_OR_KEYWORD){
			throw new SyntaxError(id, "Improper variable assignment");
		}

		if (tokenStream.getNextToken().getType() != Token.Type.ASSIGN ){
			throw new SyntaxError(id, "Improper variable assignment");
		}

		value = new Expression(this, tokenStream);
		if( !value.getType().equalsIgnoreCase(type.toString()) ){
			throw new SyntaxError(type, "Type mismatch");
		}
	}

	@Override
	public String getIdentifier() {
		return id.getValue();
	}

	@Override
	public String getType() {
		return type.getValue();
	}

	@Override
	public Object getValue() throws RuntimeError {
		return value.getValue();
	}
}
