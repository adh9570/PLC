package parsetree.builtin;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.integerexpression.IntegerExpression;
import parsetree.expressions.stringexpression.StringExpression;
import scanner.Token;

public class CharAt extends JottEntity {

    private StringExpression obj1;
    private IntegerExpression obj2;

    public CharAt(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {

        // First Parenthesis
        Token openParen = tokenStream.getNextToken();
        if(!openParen.getValue().equals("(")){
            error.throwSyntax("Expected ( got" + openParen.getValue(), openParen);
        }

        Token temp = tokenStream.peekNextToken();
        obj1 = new StringExpression(this);
        obj1.construct();
        if(!obj1.isValid()){
            error.throwSyntax("Expected String for concat's first parameter", temp);
        }
        obj1.establish();


        Token comma = tokenStream.getNextToken();
        if(!comma.getValue().equals(",")){
            error.throwSyntax("Expected , got " + openParen.getValue(), comma);
        }

        temp = tokenStream.peekNextToken();
        obj2 = new IntegerExpression(this);
        obj2.construct();
        if(!obj2.isValid()){
            error.throwSyntax("Expected Integer for concat's second parameter", temp);
        }
        obj2.establish();

        // Second Parenthesis
        Token closeParen = tokenStream.getNextToken();
        if(!closeParen.getValue().equals(")")){
            error.throwSyntax("Expected ) got " + closeParen.getValue(), closeParen);
        }
    }

    @Override
    public Class getType() {
        return String.class;
    }


    @Override
    public Object getValue() throws JottError.JottException {
        String val1 = obj1.getValue().toString();
        int val2 = Integer.parseInt(obj2.getValue().toString());

        return val1.charAt(val2);
    }

}
