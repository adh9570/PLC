package parsetree.variables.function;

import driver.JottError;
import parsetree.Statement;
import parsetree.entity.JottEntity;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class Body extends JottEntity {

    private List<JottEntity> statements;

    public Body(JottEntity parent) {
        super(parent);
        statements = new ArrayList<>();
    }

    @Override
    public void construct() throws JottError.JottException {
        Token open = tokenStream.getNextToken();
        if( !open.getValue().equals("{") ){
            error.throwSyntax("Expected { got " + open.getValue(), open);
        }

        while( !(tokenStream.isEmpty() || tokenStream.peekNextToken().getValue().equals("}")) ){
            Statement child = new Statement(this);
            child.construct();
            if(child.isValid()) {
                child.establish();
                statements.add(child);
            }
        }
        tokenStream.getNextToken();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return statements;
    }

    @Override
    public void execute() throws JottError.JottException {
        for( JottEntity s: statements){
            s.execute();
        }
    }
}
