package parsetree.variables.function;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.variables.Declaration;
import scanner.Token;

public class ParamList extends JottEntity {

    public ParamList(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        Token assigmentSign = tokenStream.getNextToken();

        if ( assigmentSign.getType() != Token.Type.START_PAREN ){
            invalidate();
            return;
        }

        if ( tokenStream.peekNextToken().getType() == Token.Type.END_PAREN ) {
            tokenStream.getNextToken();
            return;
        }

        Declaration declaration;
        Token next;
        do {
            declaration = new Declaration(this);
            declaration.construct();
            if(!declaration.isValid()){
                invalidate();
                return;
            }
            declaration.establish();
            declaration.bind(null);
            next = tokenStream.getNextToken();
        }
        while(next.getValue().equals(","));

        if(!next.getValue().equals(")")){
            invalidate();
            return;
        }

    }


    @Override
    public Object getValue() throws JottError.JottException {
        return this.children;
    }
}
