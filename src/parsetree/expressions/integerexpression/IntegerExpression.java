package parsetree.expressions.integerexpression;

import driver.JottError;
import parsetree.entity.JottEntity;
import scanner.Token;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class IntegerExpression extends JottEntity {

    private List<Factor> factorList;
    private List<String> operatorList;

    private Token initialToken;

    private static List<String> operators = Arrays.asList(
            "+", "-", "/", "*", "^"
    );

    public interface Arithmetic
        {Integer apply(int x, int y);}
    public static class Add implements Arithmetic
        { public Integer apply(int x, int y) { return x+y; } }
    public static class Subtract implements Arithmetic
        { public Integer apply(int x, int y) { return x-y; } }
    public static class Multiply implements Arithmetic
        { public Integer apply(int x, int y) { return x*y; } }
    public static class Divide implements Arithmetic {
        public Integer apply(int x, int y) {
            if(y ==0)
                return null;
            return x/y;
        }
    }
    public static class Power implements Arithmetic
        { public Integer apply(int x, int y) { return (int) Math.pow(x, y); } }

    public IntegerExpression(JottEntity parent) {
        super(parent);
        factorList = new ArrayList<>();
        operatorList = new ArrayList<>();
    }

    @Override
    public void construct() throws JottError.JottException {
        initialToken = tokenStream.peekNextToken();
        if( initialToken.getValue().equals("concat") ||  initialToken.getValue().equals("charAt")){
            invalidate();
            return;
        }

        Factor factor;
        Token operation;
        do {
            factor = new Factor(this);
            factor.construct();

            if( !factorList.isEmpty() && !factor.isValid() ){
                String message = "Type mismatch: Expected Integer got " + factor.getType().getSimpleName();
                error.throwSyntax(message, initialToken);
            }
            if(factor.isValid()){
                factorList.add(factor);
            }
            else{
                break;
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
        return Integer.class;
    }

    @Override
    public Object getValue() throws JottError.JottException {

        Map<String, Arithmetic> arithmetic = new HashMap<>();
        arithmetic.put("+", new Add());
        arithmetic.put("-", new Subtract());
        arithmetic.put("*", new Multiply());
        arithmetic.put("/", new Divide());
        arithmetic.put("^", new Power());

        Integer value = (int) factorList.get(0).getValue();
        for(int i = 1; i < factorList.size(); i++ ){
            String operation = operatorList.get(i - 1);
            //noinspection ConstantConditions
            value = arithmetic.get(operation).apply(value, (int) factorList.get(i).getValue());
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
