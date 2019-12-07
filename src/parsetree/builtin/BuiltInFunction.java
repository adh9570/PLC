package parsetree.builtin;

import driver.JottError;
import parsetree.entity.JottEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BuiltInFunction extends JottEntity {

    private static final Class[] ChildTypes = {
            Print.class,
            CharAt.class,
            Concat.class,
    };

    private JottEntity child;
    public BuiltInFunction(JottEntity parent) {
        super(parent);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void construct() throws JottError.JottException {
        child = null;

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

        if(child == null) {
            invalidate();
            return;
        }

        child.establish();
    }


    @Override
    public Class getType() {
        return child.getType();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return child.getValue();
    }
}
