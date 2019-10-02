package parsetree.expressions.Double;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.DOUBLE;

@SuppressWarnings("DuplicatedCode")
public class DoubleExpression extends GrammarObject implements GrammarValue {

    private Object obj1;
    private Token operation;
    private Object obj2;

    /**
     * Constructor( {@link GrammarObject} )
     *
     * @param parent - {@link GrammarObject} that is its parent or null
     *               if root of tree
     */
    public DoubleExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);
        Token operation = tokenStream.peekNextToken(2);

        obj1 = new Term(this, tokenStream);
        if( operation.getType() == Token.Type.ADD_OP ){
            this.operation = tokenStream.getNextToken();
            obj2 = new DoubleExpression(this, tokenStream);
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

        if(operation.getValue().equals("+"))
            return val1 + val2;
        return val1 - val2;
    }

}
