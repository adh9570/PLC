package parsetree.recursion;

import driver.JottError;
import parsetree.Statement;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import parsetree.variables.Assignment;
import parsetree.variables.Reassignment;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class For extends JottEntity {

    private Assignment assigment;
    private Expression condition;
    private Reassignment reassignment;
    private List<Statement> forStatementList;

    public For(JottEntity parent) {
        super(parent);
        forStatementList = new ArrayList<>();
    }

    @Override
    public void construct() throws JottError.JottException {
        Token _for =  tokenStream.getNextToken();
        if( !_for.getValue().equals("for") ){
            invalidate();
            return;
        }


        Token open = tokenStream.getNextToken();
        if( !open.getValue().equals("(") ){
            error.throwSyntax("Expected ( got " + open.getValue(), open);
        }

        assigment = new Assignment(this);
        assigment.construct();
        assigment.establish();

        Token endStmt = tokenStream.getNextToken();
        if( !endStmt.getValue().equals(";") ){
            error.throwSyntax("Expected ; got " + open.getValue(), open);
        }

        condition = new Expression(this);
        condition.construct();
        condition.establish();

        endStmt = tokenStream.getNextToken();
        if( !endStmt.getValue().equals(";") ){
            error.throwSyntax("Expected ; got " + open.getValue(), open);
        }

        reassignment = new Reassignment(this);
        reassignment.construct();
        reassignment.establish();


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
                forStatementList.add(child);
            }
        }
        tokenStream.getNextToken();


    }

    @Override
    public Class getType() {
        return While.class;
    }

    @Override
    public void execute() throws JottError.JottException {
        assigment.execute();

        while((int)condition.getValue() == 1){
            for( JottEntity entity : forStatementList )
                entity.execute();
            reassignment.execute();
        }
    }
}
