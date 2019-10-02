package parsetree.expressions.Double;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.DOUBLE;

@SuppressWarnings("DuplicatedCode")
class Term extends GrammarObject implements GrammarValue {

    private Object obj1;
    private Token operation;
    private Object obj2;

    Term(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        Token operation = tokenStream.peekNextToken(2);

        obj1 = new Factor(this, tokenStream);

        if( operation.getType() == Token.Type.MULT_OP ){
            this.operation = tokenStream.getNextToken();
            obj2 = new Term(this, tokenStream);
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
        if( operation == null ){
            return ((GrammarValue)obj1).getValue();
        }
        double val1 = Double.parseDouble(((GrammarValue)obj1).getValue().toString());
        double val2 = Double.parseDouble(((GrammarValue)obj2).getValue().toString());
        if(operation.getValue().equals("*"))
            return val1 * val2;
        if(val2 == 0){
            throw new RuntimeError(operation, "Divide by zero");
        }
        return val1 / val2;
    }
}
