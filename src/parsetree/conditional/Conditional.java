package parsetree.conditional;

import driver.JottError;
import parsetree.entity.JottEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Conditional extends JottEntity {

    private static final Class[] ChildTypes = {
            If.class,
    };

    private JottEntity child;

    public Conditional(JottEntity parent) throws JottError.JottException {
        super(parent);
        child = null;

        for( Class<?> type: ChildTypes ){
            try {
                Constructor<?> constructor = type.getConstructor(JottEntity.class);
                child = (JottEntity) constructor.newInstance(this);
                child.construct();
                if(!child.isValid()){
                    child = null;
                }
            }
            catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException ignored) {}
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
