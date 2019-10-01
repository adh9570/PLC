package scanner;

/**
 * Is Class
 *
 * Static class used to make code more legible
 *  to read. Holds all comparisons for Scanner.
 */
class Is {

    private Is(){}

    private static final char NEWLINE = '\n';
    private static final char SPACE = ' ';
    private static final char TAB = '\t';
    private static final char QUOTE = '"';
    private static final char PERIOD = '.';
    private static final char EQUAL = '=';
    private static final char SEMICOLON = ';';
    private static final char START_PAREN = '(';
    private static final char END_PAREN = ')';
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char MULT = '*';
    private static final char DIVIDE = '/';
    private static final char CARROT = '^';


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

    static boolean period(char c){
        return c == PERIOD;
    }

    static boolean equal(char c){
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
}
