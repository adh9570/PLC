package scanner;

/**
 * Token
 *
 * Token class is used to define types
 *  to the values that the scanner declares.
 *
 * This is the object that is used by the grammar
 *  to form the workings of the program.
 *
 */
public class Token {


	/**
     * Type
     *
     * Enum to hold the state that the
     *  token represents. This allows
     *  the grammar to create the parse
     *  tree with ease.
     */
    public enum Type {
        STRING("String"),
        ID_OR_KEYWORD(),
        NUMBER("Double"),
        ASSIGN(),
        END_STMT(),
        START_PAREN(),
        END_PAREN(),
        START_BRACKET(),
        END_BRACKET(),
        POWER(),
        COMMA(),
        EQ(),
        NOT_EQUAL(),
        ADD_OP(),
        MULT_OP(),
        LESS(),
        GREATER(),
        LESS_EQ(),
        GREATER_EQUAL();

        String value;

        Type(){
            this.value = "";
        }

        Type(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }


    /**
     * value
     *
     * String value of the token
     */
    private final String value;


    /**
     * type
     *
     * State that the token is
     */
    private final Type type;

    /**
     * line
     *
     * Line number that the token is on
     */
    private final int line;

    /**
     * Constructor(String, Token)
     *
     * @param token String representation of the token
     * @param type State that the token will represent
     */
    Token(String token, Type type, int lineNumber){
        value = token;
        this.type = type;
        line = lineNumber;
    }



    /**
     * getType()
     *
     * @return type
     */
    public Type getType() {
        return type;
    }


    /**
     * getValue
     *
     * @return value
     */
    public String getValue(){return value;}

    /**
     * getLine
     *
     * @return line
     */
    public int getLine() {
        return line;
    }

    /**
     * equals(obj)
     *
     * @param obj - String or Token to compare
     * @return boolean of equivalence
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Token){
            return value.equals(((Token) obj).value);
        }
        return this.value.equals(obj);
    }


    /**
     * hashCode
     *
     * @return super's hashCode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }


    /**
     * toString()
     *
     * @return String representation of value
     */
    @Override
    public String toString() {
        return value;
    }
}
