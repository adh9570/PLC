package parsetree.expressions.integerexpression;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.functioncall.FunctionExpression;
import scanner.Token;

import static driver.ReservedWords.isReserved;

@SuppressWarnings("DuplicatedCode")
public class Factor extends JottEntity {

    private Object value;
    private boolean isNegated;
    private Token id = null;
    private Class type = Integer.class;

    Factor(JottEntity parent) {
        super(parent);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
        FunctionExpression functionExpression = new FunctionExpression(this);
        functionExpression.construct();
        if(functionExpression.isValid()){
            id = functionExpression.getId();
            type = functionExpression.getType();
            return;
        }

        isNegated = false;
        Token val = tokenStream.getNextToken();
        if(val.getType() == Token.Type.STRING){
            type = String.class;
            invalidate();
            return;
        }

        if( val.getValue().equals("-") || val.getValue().equals("+")){
            isNegated = val.getValue().equals("-");

            value = new Factor(this);
            ((JottEntity)value).construct();
            if(!((JottEntity) value).isValid()){
                invalidate();
                return;
            }
            ((JottEntity)value).establish();
            return;

        }

        switch (val.getType()) {
            case ID_OR_KEYWORD:
                if(isReserved(val.getValue())){
                    invalidate();
                    return;
                }

                id = val;
                JottEntity _value = findInScope(id.getValue(), id);
                if( _value.getType() != Integer.class ){
                    type = _value.getType();
                    invalidate();
                    return;
                }
                break;

            case NUMBER:
                value = val.getValue();
                if(value.toString().contains(".")){
                    type = Double.class;
                    invalidate();
                    return;
                }
                break;

            case START_PAREN:
                this.value = new IntegerExpression(this);
                break;

            default:
                break;
        }

    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public Object getValue() throws JottError.JottException{
        int val = 1;
        if(isNegated)
            val = -1;

        if( id != null ){
            JottEntity _value = findInScope(id.getValue(), id);
            _value.execute();
            val *= Integer.parseInt( _value.getValue().toString() );
            return val;
        }

        if( value instanceof JottEntity){
            System.out.println(value);
            int temp = (int) ((JottEntity) value).getValue();

            val = val * temp;
        }
        else{
            val = val * Integer.parseInt(value.toString());
        }

        return val;
    }

    @Override
    public boolean isValid() {
        if( value instanceof JottEntity ) {
            return ((JottEntity) value).isValid() && ((JottEntity) value).getType() == Integer.class;
        }
        return super.isValid();
    }
}
