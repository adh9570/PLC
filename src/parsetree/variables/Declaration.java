package parsetree.variables;

import driver.JottError;
import parsetree.entity.JottEntity;
import scanner.Token;

import java.util.Arrays;
import java.util.List;

public class Declaration extends JottEntity {

    private static final List<String> TYPES = Arrays.asList(
            "String",
            "Double",
            "Integer",
            "Void"
    );
    private Token type;
    private Token id;

    public Declaration(JottEntity parent) {
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

        if( id.getType() != Token.Type.ID_OR_KEYWORD){
            error.throwSyntax("Improper variable assignment", id);
        }

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

    public void bind(Object value){
        Value v = new Value(this);
        v.setType(getType());
        v.setValue(value);
        scopeVariable(id.getValue(), v);
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return id.getValue();
    }

    public String getId() {
        return id.getValue();
    }
}
