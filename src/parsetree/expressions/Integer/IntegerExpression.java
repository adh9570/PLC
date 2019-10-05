package parsetree.expressions.Integer;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import parsetree.expressions.Expression;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.INTEGER;
import static scanner.Token.Type.*;

@SuppressWarnings("DuplicatedCode")
public class IntegerExpression extends GrammarObject implements GrammarValue {

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
    public IntegerExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        obj1 = new Factor(this, tokenStream);
        Token operation = tokenStream.peekNextToken();

        if( isMathOperator(operation)) {
            this.operation = tokenStream.getNextToken();
            obj2 = new Factor(this, tokenStream);

            if( isMathOperator(tokenStream.peekNextToken())) {
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
        return INTEGER;
    }

    @Override
    public Object getValue() throws RuntimeError {
        if( operation == null ){
            return ((GrammarValue)obj1).getValue();
        }

        int val1 = Integer.parseInt(((GrammarValue)obj1).getValue().toString());
        int val2 = Integer.parseInt(((GrammarValue)obj2).getValue().toString());
        int val3 = this.doOperation(val1, val2, operation);
        if( obj3 == null ){
            return val3;
        }
        return this.doOperation(val3, (int)((GrammarValue)obj3).getValue(), operation2);
    }

    private int doOperation(int val1, int val2, Token operation) throws RuntimeError {

        if (operation.getValue().equals("+"))
            return val1 + val2;
        if (operation.getValue().equals("-"))
            return val1 - val2;
        if (operation.getValue().equals("/")) {
            if (val2 == 0 ) {
                throw new RuntimeError(operation, "Division by 0");
            }
            return val1 / val2;
        }
        if (operation.getValue().equals("*"))
            return val1 * val2;
        if (operation.getValue().equals("^"))
            return (int) Math.pow(val1, val2);

        throw new RuntimeError(operation, "Mathematical Error");
    }

    private static boolean isMathOperator(Token token){
        Token.Type t = token.getType();
        return t == POWER || t == MULT_OP || t == ADD_OP;
    }

}
