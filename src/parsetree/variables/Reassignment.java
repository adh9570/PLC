package parsetree.variables;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

public class Reassignment extends JottEntity {

    private Token id;
    private Expression value;

    public Reassignment(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        if(tokenStream.peekNextToken(2) != null && tokenStream.peekNextToken(2).getType() != Token.Type.ASSIGN){
            invalidate();
            return;
        }
        id = tokenStream.getNextToken();
        Token assignmentSign = tokenStream.getNextToken();

        if( id.getType() != Token.Type.ID_OR_KEYWORD){
            error.throwSyntax("Improper variable assignment", id);
        }

        JottEntity origin = findInScope(id.toString());
        if( origin == null ){
            error.throwSyntax("Undeclared variable", id);
            return;
        }
        Class type = origin.getType();

        if ( assignmentSign.getType() != Token.Type.ASSIGN ){
            error.throwSyntax( "Missing = on variable assigment", id);
        }

        value = new Expression(this);
        if( value.getType() != type ){
            String message = "Invalid type in re-assignment: Expected " +
                value.getType().getSimpleName() + " got " + type.getSimpleName();
            error.throwSyntax( message, id);
        }
    }

    @Override
    public void execute() throws JottError.JottException {
        Value v = new Value(this);
        v.setValue(value.getValue());
        v.setType(value.getType());
        scopeVariable(id.getValue(), v);
    }


    @Override
    public Object getValue() throws JottError.JottException {
        return value.getValue();
    }
}

