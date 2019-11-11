package parsetree.recursion;

import driver.JottError;
import parsetree.Statement;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class While extends JottEntity {

    private Expression condition;
    private List<Statement> whileStatementList;

    public While(JottEntity parent) {
        super(parent);
        whileStatementList = new ArrayList<>();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
        Token _while =  tokenStream.getNextToken();
        if( !_while.getValue().equals("while") ){
            invalidate();
            return;
        }

        Token open = tokenStream.getNextToken();
        if( !open.getValue().equals("(") ){
            error.throwSyntax("Expected ( got " + open.getValue(), open);
        }

        condition = new Expression(this);
        condition.construct();
        condition.establish();
        if(condition.getType() != Integer.class){
            error.throwSyntax("Condition of while loop must be integer", _while);
        }

        Token close = tokenStream.getNextToken();
        if( !close.getValue().equals(")") ){
            error.throwSyntax("Expected ) got " + close.getValue(), close);
        }

        open = tokenStream.getNextToken();
        if( !open.getValue().equals("{") ){
            error.throwSyntax("Expected { got " + open.getValue(), open);
        }

        while( !(tokenStream.isEmpty() || tokenStream.peekNextToken().getValue().equals("}")) ){
            Statement child = new Statement(this);
            child.construct();
            if(child.isValid()) {
                child.establish();
                whileStatementList.add(child);
            }
        }
        tokenStream.getNextToken();
    }

    @Override
    public void execute() throws JottError.JottException {
        while((int)condition.getValue() == 1){
            for( JottEntity entity : whileStatementList )
                entity.execute();
        }
    }

    @Override
    public Class getType() {
        return While.class;
    }
}
