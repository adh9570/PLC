package parsetree.conditional;

import driver.JottError;
import parsetree.Statement;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class If extends JottEntity {

    private Expression condition;
    private List<Statement> ifStatementList;
    private List<Statement> elseStatementList;

    public If(JottEntity parent) {
        super(parent);
        ifStatementList = new ArrayList<>();
        elseStatementList = new ArrayList<>();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
        Token _if =  tokenStream.getNextToken();
        if( !_if.getValue().equals("if") ){
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
            error.throwSyntax("Condition of if statement must be integer", _if);
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
                ifStatementList.add(child);
            }
        }
        tokenStream.getNextToken();

//         ELSE
        Token _else =  tokenStream.peekNextToken();
        if( !_else.getValue().equals("else") ){
            return;
        }
        tokenStream.getNextToken();

        open = tokenStream.getNextToken();
        if( !open.getValue().equals("{") ){
            error.throwSyntax("Expected { got " + open.getValue(), open);
        }

        while( !(tokenStream.isEmpty() || tokenStream.peekNextToken().getValue().equals("}")) ){
            Statement child = new Statement(this);
            child.construct();
            if(child.isValid()) {
                child.establish();
                elseStatementList.add(child);
            }
        }
        tokenStream.getNextToken();
    }

    @Override
    public void execute() throws JottError.JottException {
        if((int)condition.getValue() == 1){
            for( JottEntity entity : ifStatementList )
                entity.execute();
        }
        else{
            for( JottEntity entity : elseStatementList )
                entity.execute();
        }
    }

    @Override
    public Class getType() {
        return If.class;
    }
}
