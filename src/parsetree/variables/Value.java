package parsetree.variables;

import parsetree.entity.JottEntity;

public class Value extends JottEntity {
    private Object value;
    private Class type;

    @SuppressWarnings("WeakerAccess")
    public Value(JottEntity parent) {
        super(parent);
    }


    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
