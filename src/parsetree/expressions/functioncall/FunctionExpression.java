package parsetree.expressions.functioncall;

import driver.JottError;
import parsetree.builtin.BuiltInFunction;
import parsetree.entity.JottEntity;
import parsetree.expressions.Expression;
import parsetree.variables.Function;
import scanner.Token;

public class FunctionExpression extends JottEntity {

    private Token id_;
    private Object value;
    private boolean isBuiltin = false;
    private BuiltInFunction builtInFunction;

    public FunctionExpression(JottEntity parent) {
        super(parent);
    }

    @Override
    public void construct() throws JottError.JottException {
        id_ = tokenStream.peekNextToken();
        builtInFunction = new BuiltInFunction(this);
        builtInFunction.construct();
        if(builtInFunction.isValid()){
            isBuiltin = true;
            return;
        }


        id_ = tokenStream.getNextToken();
        if(id_.getType() != Token.Type.ID_OR_KEYWORD){
            invalidate();
            return;
        }

        if( !tokenStream.getNextToken().getValue().equals("(") ) {
            invalidate();
            return;
        }

        while(!tokenStream.peekNextToken().getValue().equals(")")){
            Expression expression = new Expression(this);
            expression.construct();
            if(!expression.isValid()){
                invalidate();
                return;
            }
            expression.establish();
            if(tokenStream.peekNextToken().getValue().equals(",")){
                tokenStream.getNextToken();
            }
        }
        tokenStream.getNextToken();
    }

    @Override
    public Class getType() {
        if(isBuiltin){
            return builtInFunction.getType();
        }
        return findInScope(id_.getValue()).getType();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        if(isBuiltin){
            return builtInFunction.getValue();
        }
        execute();
        return value;
    }

    @Override
    public void execute() throws JottError.JottException {
        if(isBuiltin){
            builtInFunction.execute();
            return;
        }
        JottEntity bindedObject = findInScope(id_.getValue());
        if(bindedObject.getValue() instanceof Function){
            bindedObject = (JottEntity) bindedObject.getValue();
        }
        else if( !(bindedObject instanceof Function) ){
            error.throwRuntime("Function call on non-function", id_);
            return;
        }
        Function function = (Function) bindedObject;
        function.scopeVariables(children);
        function.call();
        value = function.getValue();
    }

	public Token getId() {
		return id_;
	}
}
