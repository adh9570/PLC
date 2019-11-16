package parsetree.expressions.functioncall;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import scanner.Token;

public class ReturnExpression extends JottEntity {

    private Expression expression;

    public ReturnExpression(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {

        Token return_ = tokenStream.getNextToken();
        if(!return_.getValue().equals("return")){
            invalidate();
            return;
        }

        expression = new Expression(this);
        expression.construct();
        if(!expression.isValid()){
            invalidate();
            return;
        }

    }

    @Override
    public Class getType() {
        return ReturnExpression.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return expression.getValue();
    }
}
