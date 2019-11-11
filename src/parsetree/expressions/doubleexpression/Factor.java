package parsetree.expressions.doubleexpression;

import driver.JottError;
import parsetree.entity.JottEntity;
import scanner.Token;

import static driver.ReservedWords.isReserved;

@SuppressWarnings("DuplicatedCode")
class Factor extends JottEntity {

    private Object value;
    private boolean isNegated;
    private Token id = null;
    private Class type = Double.class;

    Factor(JottEntity parent) {
        super(parent);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
        isNegated = false;
        Token val = tokenStream.getNextToken();
        if(val.getType() == Token.Type.STRING){
            invalidate();
            return;
        }

        if(val.getValue().equals("-") || val.getValue().equals("+")){
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
                value = findInScope(val.getValue(), val);
                if(((JottEntity) value).getType() != Double.class ){
                    type = ((JottEntity) value).getType();
                    invalidate();
                    return;
                }
                break;

            case NUMBER:
                value = val.getValue();
                if(!value.toString().contains(".")){
                    type = Integer.class;
                    invalidate();
                    return;
                }
                break;

            case START_PAREN:
                this.value = new DoubleExpression(this);
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
    public Object getValue() throws JottError.JottException {
        if( id != null )
            return findInScope(id.getValue(), id).getValue();

        double val = 1;
        if(isNegated)
            val = -1;

        if( value instanceof JottEntity) {
            val *= Double.parseDouble(((JottEntity) value).getValue().toString());
            return val;
        }

        return val * Double.parseDouble(value.toString());
    }

    @Override
    public boolean isValid() {
        if( value instanceof JottEntity) {
            return ((JottEntity) value).isValid() && ((JottEntity) value).getType() == Double.class;
        }
        return super.isValid();
    }
}
