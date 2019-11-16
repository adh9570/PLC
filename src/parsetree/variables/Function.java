package parsetree.variables;

import driver.JottError;
import parsetree.entity.JottEntity;
import parsetree.variables.function.Body;
import parsetree.variables.function.ParamList;
import parsetree.expressions.functioncall.ReturnExpression;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class Function extends JottEntity{

	private Token initalToken;
	private ParamList paramList;
	private Body body;
	private List<JottEntity> parameters;
	private Object value;
	private Declaration declaration;

	public Function(JottEntity parent) {
		super(parent);
		initScope();
	}


	@Override
	public void construct() throws JottError.JottException {
		initalToken = tokenStream.peekNextToken();
		declaration = new Declaration(this);
		declaration.construct();
		if(!declaration.isValid()){
			invalidate();
			return;
		}
		declaration.bind(this);

		paramList = new ParamList(this);
		paramList.construct();
		if( !paramList.isValid() ){
			invalidate();
			return;
		}

		body = new Body(this);
		body.construct();
		if(!body.isValid()){
			invalidate();
			return;
		}

		scopeVariable((String) declaration.getValue(), this);
	}

	public void scopeVariables(List<JottEntity> parameters) throws JottError.JottException {
		this.parameters = parameters;

		if( !(paramList.getValue() instanceof List)){
			return;
		}

		List declarations = (List) paramList.getValue();
		if( parameters.size() != declarations.size() ){
			error.throwRuntime("Missing parameters for function call", initalToken);
		}

		for(int i = 0; i < parameters.size(); i++){
			Declaration declaration = (Declaration) declarations.get(i);
			declaration.bind(parameters.get(i).getValue());
		}
	}

	public void call() throws JottError.JottException {
		List<JottEntity> parameters = new ArrayList<>(this.parameters);
		List<JottEntity> statements = (List<JottEntity>) body.getValue();

		for( JottEntity s: statements){
			s.execute();
			if(s.getType() == ReturnExpression.class){
				value = s.getValue();
				break;
			}
			if(!parameters.equals(this.parameters)){
				scopeVariables(parameters);
			}
		}
	}

	@Override
	public void execute() throws JottError.JottException {
		scopeVariable((String) declaration.getValue(), this);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Class getType() {
		return declaration.getType();
	}
}
