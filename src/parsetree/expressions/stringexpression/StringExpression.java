package parsetree.expressions.stringexpression;

import driver.JottError;
import parsetree.builtin.CharAt;
import parsetree.builtin.Concat;
import parsetree.entity.JottEntity;
import scanner.Token;

public class StringExpression extends JottEntity {

    private Object obj;
    private Token id = null;

    public StringExpression(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        Token token = tokenStream.getNextToken();

        if( token.getType() == Token.Type.STRING ){
            obj = token.getValue();
        }
        else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("concat")) {
            obj = new Concat(this);
            ((JottEntity) obj).construct();
            ((JottEntity) obj).establish();
        }
        else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("charAt")){
            obj = new CharAt(this);
            ((JottEntity) obj).construct();
            ((JottEntity) obj).establish();
        }
        else if (token.getType() == Token.Type.ID_OR_KEYWORD){
            id = token;
        }
        else{
            invalidate();
        }
    }

    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        if( id != null ){
            return findInScope(id.getValue(), id).getValue();
        }
        if( obj instanceof JottEntity ){
            return ((JottEntity)obj).getValue();
        }
        return obj;
    }

}
