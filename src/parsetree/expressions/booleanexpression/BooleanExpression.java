package parsetree.expressions.booleanexpression;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.expressions.doubleexpression.DoubleExpression;
import parsetree.expressions.integerexpression.IntegerExpression;
import parsetree.expressions.stringexpression.StringExpression;
import scanner.Token;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooleanExpression  extends JottEntity {

    private JottEntity obj1;
    private String operation;
    private JottEntity obj2;

    private static List<String> operators = Arrays.asList(
            "<", ">", "<=", ">=", "==", "!="
    );

    public interface Arithmetic { boolean apply(Object x, Object y);}

    public static class LessThan implements Arithmetic
        {public boolean apply(Object x, Object y) { return (int)x < (int)y; }}
    public static class LessThanEqual implements Arithmetic
        {public boolean apply(Object x, Object y) { return (int)x <= (int)y; }}
    public static class GreaterThan implements Arithmetic
        {public boolean apply(Object x, Object y) { return (int)x > (int)y; }}
    public static class GreaterThanEqual implements Arithmetic
        {public boolean apply(Object x, Object y) { return (int)x >= (int)y; }}
    public static class Equal implements Arithmetic
        {public boolean apply(Object x, Object y) { return x == y; }}
    public static class NotEqual implements Arithmetic
        {public boolean apply(Object x, Object y) { return x != y; }}

    public BooleanExpression(JottEntity parent) {
        super(parent);
    }


    @Override
    public void construct() throws JottError.JottException {
        Token initialToken = tokenStream.peekNextToken();

        obj1 = getNextExpression();
        if( obj1 == null || !obj1.isValid() ){
            invalidate();
            return;
        }

        operation = tokenStream.getNextToken().getValue();
        if( !operators.contains(operation) ){
            invalidate();
            return;
        }

        obj2 = getNextExpression();
        if( obj2 == null || !obj2.isValid() ){
            invalidate();
            return;
        }

        if(obj1.getType() != obj2.getType()){
            String message = "Invalid type in re-assignment: Expected " +
                obj1.getType().getSimpleName() + " got " + obj2.getType().toString();
            error.throwSyntax( message, initialToken);
        }
    }

    @Override
    public Class getType() {
        return Integer.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        Object v1 = obj1.getValue();
        Object v2 = obj2.getValue();

        Map<String, Arithmetic> arithmetic = new HashMap<>();
        arithmetic.put("<", new LessThan());
        arithmetic.put(">", new GreaterThan());
        arithmetic.put("<=", new LessThanEqual());
        arithmetic.put(">=", new GreaterThanEqual());
        arithmetic.put("==", new Equal());
        arithmetic.put("!=", new NotEqual());

        if( arithmetic.get(operation).apply(v1, v2) )
            return 1;
        return 0;
    }


    private static final Class[] ChildTypes = {
            IntegerExpression.class,
            DoubleExpression.class,
            StringExpression.class,
    };

    @SuppressWarnings("Duplicates")
    private JottEntity getNextExpression() throws JottError.JottException {
        JottEntity child = null;

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
                //Ignored
            }
        }

        if(child == null) {
            return null;
        }

        child.establish();
        return child;
    }
}
