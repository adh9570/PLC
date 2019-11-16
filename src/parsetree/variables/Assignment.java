package parsetree.variables;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

import java.util.Arrays;
import java.util.List;

public class Assignment extends JottEntity {

    private static final List<String> TYPES = Arrays.asList(
            "String",
            "Double",
            "Integer"
    );

    private Declaration declaration;
    private Expression value;

    public Assignment(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        Token initialToken = tokenStream.peekNextToken();
        declaration = new Declaration(this);
        declaration.construct();
        if(!declaration.isValid()){
            invalidate();
            return;
        }

        Token assigmentSign = tokenStream.getNextToken();
        if ( assigmentSign.getType() != Token.Type.ASSIGN ){
            error.throwSyntax( "Missing = on variable assigment", initialToken);
        }

        value = new Expression(this);
        value.construct();

        if( value.getType() != declaration.getType() ){
            String message = "Type mismatch: Expected " + declaration.getType()
                + " got " + value.getType().getSimpleName();
            error.throwSyntax( message, initialToken);
        }

        declaration.bind(null);
    }

    @Override
    public Class getType() {
        return declaration.getType();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return value.getValue();
    }

    @Override
    public void execute() throws JottError.JottException {
        declaration.bind(value.getValue());
    }
}
