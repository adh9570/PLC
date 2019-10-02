package parsetree;

import errors.RuntimeError;

import java.util.ArrayList;
import java.util.List;

/**
 * GrammarObject (Abstract Class)
 *
 * This class is meant to provide
 *  a easy to use tree structure for the
 *  parse tree.
 *
 * This class also incorporates scope
 *  helper functions that will scan the
 *  tree for an identifier. Works closely
 *  with {@link GrammarValue}
 *
 * This class lastly handles the default
 *  run case for every grammarObject
 */
public abstract class GrammarObject {

    private final GrammarObject parent;
    private List<GrammarObject> children;

    /**
     * Constructor( {@link GrammarObject} )
     *
     * @param parent - {@link GrammarObject} that is its parent or null
     *               if root of tree
     */
    protected GrammarObject(GrammarObject parent){
        this.parent = parent;
        children = new ArrayList<>();
        if(parent != null)
            this.parent.addChild(this);
    }


    /**
     * addChild ( GrammarObject )
     *
     * @param child - new child to the tree's node
     */
    private void addChild(GrammarObject child){
        children.add(child);
    }


    /**
     * getChildren
     *
     * @return List of all children
     */
    List<GrammarObject> getChildren() {
        return children;
    }

    /**
     * getScope
     *
     * @param identifier - ID that we are looking for
     * @return GrammarValue that matches the keyword
     */
    protected GrammarValue getScope(String identifier) {
        return getScope(identifier, new ArrayList<>());
    }

    /**
     * getScope
     *
     * @param identifier - ID that we are looking for
     * @param visited - List of all Visited Nodes
     * @return GrammarValue that matches the keyword
     */
    private GrammarValue getScope(String identifier, List<GrammarObject> visited){
        if( this instanceof GrammarValue ) {
            String thisIdentifier = ((GrammarValue) this).getIdentifier();
            if (thisIdentifier != null && thisIdentifier.equals(identifier)) {
                return (GrammarValue)this;
            }
        }
        visited.add(this);

        for( GrammarObject child : children ){
            GrammarObject temp = null;

            if(!visited.contains(child)) {
                temp = (GrammarObject) child.getScope(identifier, visited);
            }

            if(temp != null)
                return (GrammarValue)temp;
        }

        if( parent == null )
            return null;
        return parent.getScope(identifier, visited);
    }

    /**
     * run()
     *
     * Runs all objects in the tree.
     */
    public void run() throws RuntimeError{
        for( GrammarObject stmt: getChildren() ){
            stmt.run();
        }
    }
}
