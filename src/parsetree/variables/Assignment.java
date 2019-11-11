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
    private Token type;
    private Token id;
    private Expression value;

    public Assignment(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        type = tokenStream.getNextToken();
        if( !TYPES.contains(type.getValue()) ){
            invalidate();
            return;
        }

        id = tokenStream.getNextToken();
        Token assigmentSign = tokenStream.getNextToken();

        if( id.getType() != Token.Type.ID_OR_KEYWORD){
            error.throwSyntax("Improper variable assignment", id);
        }

        if ( assigmentSign.getType() != Token.Type.ASSIGN ){
            error.throwSyntax( "Missing = on variable assigment", id);
        }

        value = new Expression(this);
        value.construct();
        value.establish();

        if( !value.getType().getSimpleName().equals(type.toString()) ){
            String message = "Type mismatch: Expected " +type.toString()
                + " got " + value.getType().getSimpleName();
            error.throwSyntax( message, type);
        }

        Value v = new Value(this);
        v.setValue(value.getValue());
        v.setType(value.getType());
        scopeVariable(id.getValue(), v);
    }

    @Override
    public Class getType() {
        if ("String".equals(type.getValue())) {
            return String.class;
        }
        else if ("Integer".equals(type.getValue())) {
            return Integer.class;
        }
        else if ("Double".equals(type.getValue())) {
            return Double.class;
        }
        return null;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return value.getValue();
    }

    @Override
    public void execute() throws JottError.JottException {
        Value v = new Value(this);
        v.setValue(value.getValue());
        v.setType(value.getType());
        scopeVariable(id.getValue(), v);
    }
}
