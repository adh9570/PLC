package parsetree.expressions.Double;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.DOUBLE;

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
            value = new Factor(this, tokenStream);
            return;
        }
        else if(val.getValue().equals("+")){
            value = new Factor(this, tokenStream);
            return;
        }


        switch (val.getType()) {
            case ID_OR_KEYWORD:
                value = getScope(val.getValue());
                if(!((GrammarValue) value).getType().equals(DOUBLE)){
                    throw new SyntaxError(val, "Type mismatch: Expected Double got " +
                            ((GrammarValue) value).getType());
                }
                break;
            case NUMBER:
                value = val.getValue();
                if(!value.toString().contains(".")){
                    throw new SyntaxError(val, "Type mismatch: Expected Double got Integer");
                }
                break;
            case START_PAREN:
                this.value = new DoubleExpression(this, tokenStream);
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
        return DOUBLE;
    }

    @Override
    public Object getValue() throws RuntimeError {
        double val = 1;
        if(isNegated)
            val = -1;

        if( value instanceof GrammarValue ) {
            val *= Double.parseDouble(((GrammarValue) value).getValue().toString());
            return val;
        }

        return val * Double.parseDouble(value.toString());
    }
}
