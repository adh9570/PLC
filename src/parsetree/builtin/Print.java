package parsetree.builtin;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

import static java.lang.System.out;

public class Print extends JottEntity {

    private Expression expression;

    public Print(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {

        Token nextToken = tokenStream.getNextToken();
        String token = nextToken.toString();
        if( !token.equals("print")){
            this.invalidate();
            return;
        }

        Token openParen = tokenStream.getNextToken();
        //noinspection EqualsBetweenInconvertibleTypes
        if( !openParen.getValue().equals("(") ){
            String errorMessage =  "Syntax Error: Expected ( got " + openParen.getValue();
            error.throwSyntax( errorMessage, openParen);
        }

        expression = new Expression(this);
        expression.construct();
        if(!expression.isValid()){
            invalidate();
            return;
        }
        expression.establish();

        Token closedParen = tokenStream.getNextToken();
        //noinspection EqualsBetweenInconvertibleTypes
        if( !closedParen.getValue().equals(")") ){
            String errorMessage =  "Expected ) got " + closedParen.getValue();
            error.throwSyntax( errorMessage, closedParen);
        }

    }

    @Override
    public void execute() throws JottError.JottException {
        out.println(expression.getValue());
    }

    @Override
    public Class getType() {
        return Print.class;
    }
}
