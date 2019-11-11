package parsetree.entity;

import driver.JottError;
import scanner.Token;
import scanner.TokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JottEntity implements ParseTreeEntity, RunTimeEntity {

    private final JottEntity parent;
    private boolean hasScope;
    private Map<String, JottEntity> scope;
    protected final TokenStream tokenStream;
    private final List<JottEntity> children;
    protected final JottError error;
    protected boolean valid;
    private int start;

    public JottEntity(TokenStream tokenStream, JottError error) {
        this.parent = null;
        this.tokenStream = tokenStream;
        this.error = error;
        this.children = new ArrayList<>();

        valid=true;
        start = tokenStream.getIndex();
    }

    public JottEntity(JottEntity parent) {
        this.parent = parent;
        this.tokenStream = parent.tokenStream;
        this.error = parent.error;
        this.children = new ArrayList<>();

        valid=true;
        start = tokenStream.getIndex();
    }


    @Override
    public void construct() throws JottError.JottException {
        // Ignored
    }

    @Override
    public void establish() throws JottError.JottException {
        if(parent != null)
            parent.children.add(this);
        else
            throw new JottError.JottException();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    public void invalidate(){
        valid = false;
        reset();
    }

    @Override
    public Class getType() {
        return null;
    }

    @Override
    public void execute() throws JottError.JottException {
        for ( JottEntity child : children )
            child.execute();
    }

    @Override
    public Object getValue() throws JottError.JottException {
        return null;
    }

    @Override
    public void initScope(){
        hasScope = true;
        scope = new HashMap<>();
    }

    @Override
    public void scopeVariable(String keyword, JottEntity value) {
        JottEntity parentScope = this;
        while( parentScope != null ){
            if(parentScope.hasScope){
                parentScope.scope.put(keyword, value);
                return;
            }

            parentScope = parentScope.parent;
        }
    }


    @Override
    public JottEntity findInScope(String keyword, Token errorToken) throws JottError.JottException {
        JottEntity parentScope = this;
        while( parentScope != null ){
            if(parentScope.hasScope && parentScope.scope.containsKey(keyword)){
                return parentScope.scope.get(keyword);
            }

            parentScope = parentScope.parent;
        }

        error.throwRuntime("Variable not initialized", errorToken);
        return null;
    }

    @Override
    public JottEntity findInScope(String keyword) {
        JottEntity parentScope = this;
        while( parentScope != null ){
            if(parentScope.hasScope && parentScope.scope.containsKey(keyword)) {
                    return parentScope.scope.get(keyword);
            }

            parentScope = parentScope.parent;
        }

        return null;
    }

    @Override
    public void reset() {
        tokenStream.setIndex(start);
    }
}
