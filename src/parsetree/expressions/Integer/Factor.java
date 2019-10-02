package parsetree.expressions.Integer;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.INTEGER;

@SuppressWarnings("DuplicatedCode")
class Factor extends GrammarObject implements GrammarValue {

    private Object value;
    private boolean isNegated;

    /**
     * Constructor( {@link GrammarObject} )
     *
     * @param parent - {@link GrammarObject} that is its parent
     */
    Factor(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        isNegated = false;
        Token val = tokenStream.getNextToken();

        if(val.getValue().equals("-")){
            isNegated = true;
            val = tokenStream.getNextToken();
        }


        switch (val.getType()) {
            case ID_OR_KEYWORD:
                value = getScope(val.getValue());
                if(!((GrammarValue) value).getType().equals(INTEGER)){
                    throw new SyntaxError(val, "Type mismatch: Expected Integer got " +
                            ((GrammarValue) value).getType());
                }
                else if(value == null){
                    throw new SyntaxError(val, "Undeclared variable");
                }
                break;
            case NUMBER:
                value = val.getValue();
                if(value.toString().contains(".")){
                    throw new SyntaxError(val, "Type mismatch: Expected Integer got Double");
                }
                break;
            case START_PAREN:
                this.value = new IntegerExpression(this, tokenStream);
                break;
            default:
                break;
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public String getType() {
        return INTEGER;
    }

    @Override
    public Object getValue() throws RuntimeError {
        int val = 1;
        if(isNegated)
            val = -1;

        if( value instanceof GrammarValue ) {
            val *= Integer.parseInt(((GrammarValue) value).getValue().toString());
            return val;
        }

        return val * Integer.parseInt(value.toString());
    }
}
