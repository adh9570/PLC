package scanner;

import errors.SyntaxError;
import scanner.Token.Type;

import static scanner.Is.*;

/**
 * Scanner Class
 *
 *  The scanning class is used to parse the code into
 *      a token stream. See {@link TokenStream}
 */
public class Scanner {

    /**
     * preScanned
     *
     * Pre-scanned string
     */
    private final String preScanned;

    /**
     * Constructor(String)
     *
     * @param str - String to be scanned
     */
    public Scanner(String str) {
        preScanned = str;
    }


    /**
     * scan()
     *
     * Scan is used to print the errors that happen
     *  durring the scanning phase
     *
     * @return TokenStream - tokenStream derived from preScanned
     */
    public TokenStream scan() throws SyntaxError{

        String pre = preScanned.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1");
        TokenStream tokenStream = new TokenStream();
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
                        throw new SyntaxError(new Token(token.toString(), Type.STRING, lineNumber),"Missing \"");
                    }
                    break;
                case PERIOD:
                    boolean hasPeriod = Is.period(c);

                    while( Is.period(c) || Is.digit(c) ){
                        if( Is.period(c) ){
                            if( hasPeriod ){
                                throw new SyntaxError(new Token("", Type.NUMBER, lineNumber), "To many decimal places");
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
                        tokenStream.addToken(new Token(token.toString(), Type.GREATER_EQUAL, lineNumber));
                    }
                    else {
                        throw new SyntaxError(new Token(token.toString(), null, lineNumber),
                                "Dangling exclamation mark");
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
                            throw new SyntaxError(new Token("", Type.NUMBER, lineNumber), "To many decimal places");
                        }
                        hasPeriod = true;
                    }

                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new SyntaxError(new Token("", Type.END_STMT, lineNumber), "missing semi-colon");
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
