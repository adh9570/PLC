package scanner;

/**
 * Is Class
 *
 * Static class used to make code more legible
 *  to read. Holds all comparisons for Scanner.
 */
class Is {


    private Is(){}

    public static final char NEWLINE = '\n';
    public static final char RETURN_LINE = '\r';
    public static final char SPACE = ' ';
    public static final char TAB = '\t';
    public static final char QUOTE = '"';
    public static final char PERIOD = '.';
    public static final char EQUAL = '=';
    public static final char SEMICOLON = ';';
    public static final char START_PAREN = '(';
    public static final char END_PAREN = ')';
    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char MULT = '*';
    public static final char DIVIDE = '/';
    public static final char CARROT = '^';
    public static final char COMMA = ',';
    public static final char LESS_THAN = '<';
    public static final char GREATER_THAN = '>';
    public static final char EXCLAMATION = '!';


    static boolean character(char c){
        return Character.isUpperCase(c) || Character.isLowerCase(c);
    }

    static boolean digit(char c){
        return Character.isDigit(c);
    }

    static boolean characterOrDigit(char c){
        return character(c) || digit(c);
    }

    static boolean quote(char c){
        return c == QUOTE;
    }

    static boolean tab(char c){
        return c == TAB;
    }

    static boolean space(char c){
        return c == SPACE;
    }

    static boolean newLine(char c){
        return c == NEWLINE;
    }

    static boolean breakLine(char c){
        return c == RETURN_LINE;
    }

    static boolean period(char c){
        return c == PERIOD;
    }

    static boolean equalSign(char c){
        return c == EQUAL;
    }

    static boolean semicolon(char c) {
        return c == SEMICOLON;
    }

    static boolean startParen(char c) {
        return c == START_PAREN;
    }

    static boolean endParen(char c) {
        return c == END_PAREN;
    }

    static boolean carrot(char c) {
        return c == CARROT;
    }

    static boolean div(char c) {
        return c == DIVIDE;
    }

    static boolean mult(char c) {
        return c == MULT;
    }

    static boolean plus(char c) {
        return c == PLUS;
    }

    static boolean minus(char c) {
        return c == MINUS;
    }

    static boolean comma(char c) {
        return c == COMMA;
    }

    static boolean lessThan(char c) {
        return c == LESS_THAN;
    }

    static boolean greaterThan(char c) {
        return c == GREATER_THAN;
    }

    static boolean exclamation(char c) {
        return c == EXCLAMATION;
    }
}
