package parsetree.expressions.Double;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import parsetree.expressions.Expression;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.DOUBLE;
import static scanner.Token.Type.*;

@SuppressWarnings("DuplicatedCode")
public class DoubleExpression extends GrammarObject implements GrammarValue {

    private Object obj1;
    private Token operation;
    private Object obj2;
    private Token operation2;
    private Object obj3;

    /**
     * Constructor( {@link GrammarObject} )
     *
     * @param parent - {@link GrammarObject} that is its parent or null
     *               if root of tree
     */
    public DoubleExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        obj1 = new Factor(this, tokenStream);
        Token operation = tokenStream.peekNextToken();

        if( isMathOperator(operation) ) {
            this.operation = tokenStream.getNextToken();
            obj2 = new Factor(this, tokenStream);

            if( isMathOperator(tokenStream.peekNextToken()) ) {
                operation2 = tokenStream.getNextToken();
                obj3 = new Expression(parent, tokenStream);
            }
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
        double val3 = this.doOperation(val1, val2, operation);
        if( obj3 == null ){
            return val3;
        }
        return this.doOperation(val3, (double)((GrammarValue)obj3).getValue(), operation2);
    }

    private double doOperation(double val1, double val2, Token operation) throws RuntimeError {

        if (operation.getValue().equals("+"))
            return val1 + val2;
        if (operation.getValue().equals("-"))
            return val1 - val2;
        if (operation.getValue().equals("/")) {
            if (val2 == 0 ) {
                throw new RuntimeError(operation, "Divide by zero");
            }
            return val1 / val2;
        }
        if (operation.getValue().equals("*"))
            return val1 * val2;
        if (operation.getValue().equals("^"))
            return Math.pow(val1, val2);

        throw new RuntimeError(operation, "Mathematical Error");
    }

    private static boolean isMathOperator(Token token){
        Token.Type t = token.getType();
        return t == POWER || t == MULT_OP || t == ADD_OP;
    }
}
