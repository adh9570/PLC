package parsetree;

import errors.RuntimeError;

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
    String getIdentifier();

    /**
     * getType
     *
     * @return Type of the Value
     */
    String getType();

    /**
     * getValue
     *
     * @return Value in corresponding type
     */
    Object getValue() throws RuntimeError;
}
