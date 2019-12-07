package parsetree.builtin;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.stringexpression.StringExpression;
import scanner.Token;

public class Concat extends JottEntity {

    private StringExpression obj1;
    private StringExpression obj2;

    public Concat(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {

        Token nextToken = tokenStream.getNextToken();
        String token = nextToken.toString();
        if( !token.equals("concat")){
            this.invalidate();
            return;
        }

        // First Parenthesis
        Token openParen = tokenStream.getNextToken();
        if( openParen.getType() != Token.Type.START_PAREN ){
            error.throwSyntax("Expected ( got " + openParen.getValue(), openParen);
        }

        obj1 = new StringExpression(this);
        obj1.construct();
        if(!obj1.isValid()){
            obj1.reset();
            Token t = tokenStream.getNextToken();
            error.throwSyntax("Expected String got " + t.getValue(), t);
        }
        obj1.establish();

        Token comma = tokenStream.getNextToken();
        if( comma.getType() != Token.Type.COMMA ){
            error.throwSyntax("Expected , got " + comma.getValue(), comma);
        }

        obj2 = new StringExpression(this);
        obj2.construct();
        if(!obj2.isValid()){
            obj2.reset();
            Token t = tokenStream.getNextToken();
            error.throwSyntax("Expected String got " + t.getValue(), t);
        }
        obj2.establish();

        // Second Parenthesis
        Token closeParen = tokenStream.getNextToken();
        if( closeParen.getType() != Token.Type.END_PAREN ){
            error.throwSyntax("Expected ) got" + closeParen.getValue(), closeParen);
        }
    }

    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        String val1 = obj1.getValue().toString();
        String val2 = obj2.getValue().toString();

        return val1 + val2;
    }
}
