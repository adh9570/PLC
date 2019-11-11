package scanner;

import driver.JottError;
import scanner.Token.Type;

import static scanner.Is.*;

/**
 * Scanner Class
 *
 *  The scanning class is used to parse the code into
 *      a token stream. See {@link TokenStream}
 */
public class  Scanner {

    /**
     * preScanned
     *
     * Pre-scanned string
     */
    private final String preScanned;

    private JottError error;

    /**
     * Constructor(String)
     *
     * @param str - String to be scanned
     */
    public Scanner(String str, JottError error) {
        preScanned = str;
        this.error = error;
    }


    /**
     * scan()
     *
     * Scan is used to print the errors that happen
     *  durring the scanning phase
     *
     * @return TokenStream - tokenStream derived from preScanned
     */
    public TokenStream scan() throws JottError.JottException {

        String pre = preScanned.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1");
        TokenStream tokenStream = new TokenStream(pre);
        error.setTokenStream(tokenStream);
        char[] characters = pre.toCharArray();
        int i = 0;
        int lineNumber = 1;

        while( i < characters.length ){
            StringBuilder token = new StringBuilder();
            char c = characters[i];

            switch (c){
                case NEWLINE:
                    lineNumber++;
                case RETURN_LINE:
                case SPACE:
                case TAB:
                    i++;
                    break;
                case QUOTE:
                    i++;
                    c = characters[i];

                    while( Is.characterOrDigit(c) || Is.space(c) ){
                        token.append(c);
                        i++;
                        if( i >= characters.length )
                            break;
                        c = characters[i];
                    }

                    if( Is.quote(c) ){
                        tokenStream.addToken(new Token(token.toString(), Type.STRING, lineNumber));
                        i++;
                    }
                    else{
                        Token token_ = new Token(token.toString(), Type.STRING, lineNumber);
                        error.throwSyntax("Missing \"", token_);
                    }
                    break;
                case PERIOD:
                    boolean hasPeriod = Is.period(c);

                    while( Is.period(c) || Is.digit(c) ){
                        if( Is.period(c) ){
                            if( hasPeriod ){
                                Token token_ = new Token("", Type.NUMBER, lineNumber);
                                error.throwSyntax("To many decimal places", token_);
                            }
                            hasPeriod = true;
                        }

                        token.append(c);
                        i++;
                    }

                    tokenStream.addToken(new Token(token.toString(), Type.NUMBER, lineNumber));
                    break;
                case EQUAL:
                    token.append(c);
                    i++;
                    c = characters[i];
                    if( Is.equalSign(c) ){
                        token.append(c);
                        i++;
                        tokenStream.addToken(new Token(token.toString(), Type.EQ, lineNumber));
                    }
                    else
                        tokenStream.addToken(new Token(token.toString(), Type.ASSIGN, lineNumber));
                    break;
                case SEMICOLON:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.END_STMT, lineNumber));
                    i++;
                    break;
                case START_PAREN:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.START_PAREN, lineNumber));
                    i++;
                    break;
                case END_PAREN:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.END_PAREN, lineNumber));
                    i++;
                    break;
                case START_BRACKET:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.START_BRACKET, lineNumber));
                    i++;
                    break;
                case END_BRACKET:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.END_BRACKET, lineNumber));
                    i++;
                    break;
                case COMMA:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.COMMA, lineNumber));
                    i++;
                    break;
                case PLUS:
                case MINUS:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.ADD_OP, lineNumber));
                    i++;
                    break;
                case DIVIDE:
                case MULT:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.MULT_OP, lineNumber));
                    i++;
                    break;
                case CARROT:
                    token.append(c);
                    tokenStream.addToken(new Token(token.toString(), Type.POWER, lineNumber));
                    i++;
                    break;
                case LESS_THAN:
                    token.append(c);
                    i++;
                    c = characters[i];
                    if( Is.equalSign(c) ){
                        token.append(c);
                        i++;
                        c = characters[i];
                        tokenStream.addToken(new Token(token.toString(), Type.LESS_EQ, lineNumber));
                        i++;
                    }
                    else {
                        tokenStream.addToken(new Token(token.toString(), Type.LESS, lineNumber));
                    }
                    break;
                case GREATER_THAN:
                    token.append(c);
                    i++;
                    c = characters[i];
                    if( Is.equalSign(c) ){
                        token.append(c);
                        i++;
                        c = characters[i];
                        tokenStream.addToken(new Token(token.toString(), Type.GREATER_EQUAL, lineNumber));
                    }
                    else {
                        tokenStream.addToken(new Token(token.toString(), Type.GREATER, lineNumber));
                    }
                    break;
                case EXCLAMATION:
                    token.append(c);
                    i++;
                    c = characters[i];
                    if( Is.equalSign(c) ){
                        token.append(c);
                        i++;
                        c = characters[i];
                        tokenStream.addToken(new Token(token.toString(), Type.NOT_EQUAL, lineNumber));
                    }
                    else {
                        Token token_ = new Token(token.toString(), null, lineNumber);
                        error.throwSyntax("Dangling exclamation mark", token_);
                    }
                    break;
                default:
                    break;
            }


            // ID or Keyword
            if( Is.character(c) ){
                while( Is.characterOrDigit(c) ){
                    token.append(c);
                    i++;
                    c = characters[i];
                }
                tokenStream.addToken(new Token(token.toString(), Type.ID_OR_KEYWORD, lineNumber));
            }

            // Number
            else if( Is.period(c) || Is.digit(c) ){
                boolean hasPeriod = Is.period(c);

                while( Is.period(c) || Is.digit(c) ){
                    if( Is.period(c) ){
                        if( hasPeriod ){
                            Token token_ = new Token("", Type.NUMBER, lineNumber);
                            error.throwSyntax("To many decimal places", token_);
                        }
                        hasPeriod = true;
                    }

                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        Token token_ = new Token("", Type.END_STMT, lineNumber);
                        error.throwSyntax("missing semi-colon", token_);
                    }
                    c = characters[i];
                }

                tokenStream.addToken(new Token(token.toString(), Type.NUMBER, lineNumber));
            }

        }

        return tokenStream;
    }

    public String getLine(int linenumber){
        String[] lines =  preScanned.split("\n");
        if( linenumber <= lines.length )
            return lines[linenumber-1];
        return "";
    }

}
