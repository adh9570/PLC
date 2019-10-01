package parsetree;

import errors.SyntaxError;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.*;

class Assignment extends GrammarObject implements GrammarValue{

	private final Token id;
	private final Token type;
	private Object value;


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

		value = tokenStream.getNextToken();
		switch (type.getValue()) {
			case INTEGER:
				value = Integer.parseInt(value.toString());
				break;
			case (DOUBLE):
				value = Double.parseDouble(value.toString());
				break;
			case (STRING):
				value = value.toString();
				break;
			default:
				break;
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
	public Object getValue() {
		return value;
	}
}
