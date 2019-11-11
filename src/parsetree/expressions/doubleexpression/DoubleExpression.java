package parsetree.expressions.doubleexpression;

import driver.JottError;
import parsetree.entity.JottEntity;
import scanner.Token;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class DoubleExpression extends JottEntity {

    private List<Factor> factorList;
    private List<String> operatorList;

    private Token initialToken;

    private static List<String> operators = Arrays.asList(
            "+", "-", "/", "*", "^"
    );

    public interface Arithmetic { Double apply(double x, double y); }

    public static class Add implements Arithmetic
        { public Double apply(double x, double y) { return x+y; } }
    public static class Subtract implements Arithmetic
        { public Double apply(double x, double y) { return x-y; } }
    public static class Multiply implements Arithmetic
        { public Double apply(double x, double y) { return x*y; } }
    public static class Divide implements Arithmetic {
        public Double apply(double x, double y) {
            if(y ==0)
                return null;
            return x/y;
        }
    }
    public static class Power implements Arithmetic
        { public Double apply(double x, double y) { return Math.pow(x, y); } }

    @SuppressWarnings("WeakerAccess")
    public DoubleExpression(JottEntity parent) {
        super(parent);

        factorList = new ArrayList<>();
        operatorList = new ArrayList<>();
    }

    @Override
    public void construct() throws JottError.JottException {
        initialToken = tokenStream.peekNextToken();
        Factor factor;
        Token operation;
        do {
            factor = new Factor(this);
            factor.construct();

            if( !factorList.isEmpty() && factor.getType() != Double.class ){
                String message = "Type mismatch: Expected Double got " + factor.getType().getSimpleName();
                error.throwSyntax(message, initialToken);
            }
            if(factor.isValid()){
                factorList.add(factor);
            }
            else{
                invalidate();
                return;
            }

            operation = tokenStream.peekNextToken();
            if( operators.contains(operation.getValue())) {
                operatorList.add(operation.toString());
                tokenStream.getNextToken();
            }
            else{
                break;
            }

        } while(factor.isValid());

        if(factorList.isEmpty())
            invalidate();

    }

    @Override
    public Class getType() {
        return Double.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {
        Map<String, Arithmetic> arithmetic = new HashMap<>();
        arithmetic.put("+", new Add());
        arithmetic.put("-", new Subtract());
        arithmetic.put("*", new Multiply());
        arithmetic.put("/", new Divide());
        arithmetic.put("^", new Power());

        Double value = (Double) factorList.get(0).getValue();
        for(int i = 1; i < factorList.size(); i++ ){
            String operation = operatorList.get(i - 1);
            //noinspection ConstantConditions
            value = arithmetic.get(operation).apply(value, (double) factorList.get(i).getValue());
            if(value == null)
                error.throwRuntime("Divide by zero", initialToken);
        }
        return value;

    }


    @Override
    public boolean isValid() {
        if(!valid)
            return false;
        for( Factor factor: factorList){
            if(!factor.isValid())
                return false;
        }

        return true;
    }
}
