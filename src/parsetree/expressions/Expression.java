package parsetree.expressions;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.booleanexpression.BooleanExpression;
import parsetree.expressions.doubleexpression.DoubleExpression;
import parsetree.expressions.functioncall.FunctionExpression;
import parsetree.expressions.integerexpression.IntegerExpression;
import parsetree.expressions.functioncall.ReturnExpression;
import parsetree.expressions.stringexpression.StringExpression;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Expression extends JottEntity {

    private JottEntity child;

    private static final Class[] ChildTypes = {
            ReturnExpression.class,
            BooleanExpression.class,
            IntegerExpression.class,
            DoubleExpression.class,
            StringExpression.class,
            FunctionExpression.class,
    };

    public Expression(JottEntity parent) throws JottError.JottException {
        super(parent);

        for( Class<?> type: ChildTypes ){
            try {
                Constructor<?> constructor = type.getConstructor(JottEntity.class);
                child = (JottEntity) constructor.newInstance(this);
                child.construct();
                if(child.isValid()){
                    break;
                }
                child = null;
            }
            catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException ignored) {
                //ignored
            }
        }

        if(child == null) {
            invalidate();
            return;
        }

        child.establish();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return child.getValue();
    }

    @Override
    public boolean isValid() {
        return child != null;
    }

    @Override
    public Class getType() {
        return child.getType();
    }
}
