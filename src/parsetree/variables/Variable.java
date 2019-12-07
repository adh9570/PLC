package parsetree.variables;

import driver.JottError;
import parsetree.entity.JottEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Variable extends JottEntity {

    private JottEntity child = null;


    private static final Class[] ChildTypes = {
            Assignment.class,
            Reassignment.class,
            Function.class
    };

    public Variable(JottEntity parent) {
        super(parent);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
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
                // Ignored
            }
        }

        if(child == null ){
            invalidate();
            return;
        }

        child.establish();
    }

    @Override
    public Class getType() {
        return child.getType();
    }
}
