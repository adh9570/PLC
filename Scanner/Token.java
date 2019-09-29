package Scanner;

public class Token {
    enum Type {
        STRING,
        ID_OR_KEYWORD,
        NUMBER,
        ASSIGN,
        END_STMT,
        START_PAREN,
        END_PAREN,
        POWER,
        DIVIDE,
        MULT,
        MINUS,
        PLUS
    }
    private String value;
    private Token.Type type;

    Token(String token, Token.Type t){
        value = token;
        type = t;
    }

    @Override
    public String toString() {
        return "Value: " + value + "\tType:" + type.toString();
    }
}
