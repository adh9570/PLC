package parsetree;

/**
 * GrammarValue (Interface)
 *
 * This interface is used to handle scope
 *  identifiers and such.
 */
public interface GrammarValue {

    /**
     * getIdentifier
     *
     * @return A string that is unique to the program
     *  if not unique than is matched to scope.
     */
    public String getIdentifier();

    /**
     * getType
     *
     * @return Type of the Value
     */
    public String getType();

    /**
     * getValue
     *
     * @return Value in corresponding type
     */
    public Object getValue();
}
