package parsetree;

import driver.JottError;
import parsetree.builtin.BuiltInFunction;
import parsetree.conditional.Conditional;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import parsetree.recursion.Recursion;
import parsetree.variables.Variable;
import scanner.Token;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Statement extends JottEntity {

    private static final Class[] ChildTypes = {
            Variable.class,
            Conditional.class,
            Recursion.class,
            BuiltInFunction.class,
            Expression.class,
    };

    public Statement(JottEntity parent) {
        super(parent);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {

        JottEntity child = null;

        for( Class<?> type: ChildTypes ){
            try {
                Constructor<?> constructor = type.getConstructor(JottEntity.class);
                child = (JottEntity) constructor.newInstance(this);
                child.construct();
                Token end = tokenStream.getNextToken();
                if (end.getType() != Token.Type.END_STMT) {
                    child.invalidate();
                }
                if(child.isValid()){
                    break;
                }
                child = null;
            }
            catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException ignored) {
                // Ignored
            }
        }

        if(child == null ){
            invalidate();
            return;
        }

        child.establish();
    }

}
