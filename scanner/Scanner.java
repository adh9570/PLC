package scanner;

import errors.SyntaxError;
import scanner.Token.Type;

import static java.lang.System.*;
import static javax.xml.stream.XMLStreamConstants.SPACE;

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
    public TokenStream scan(){
        try {
            return scanThrowsError();
        }
        catch(Exception e){
            err.println(e.getMessage());
        }
        return null;
    }

    /**
     * scan_throws_error
     *
     * @return TokenStream - tokenStream derived from preScanned
     * @throws SyntaxError - Any error that may have occured
     *  durring the scanning phase
     */
    private TokenStream scanThrowsError() throws SyntaxError{

        TokenStream tokenStream = new TokenStream();
        char[] characters = preScanned.toCharArray();
        int i = 0;

        while( i < characters.length ){
            StringBuilder token = new StringBuilder();
            char c = characters[i];

            // Nothing
            if(Is.newLine(c) || Is.space(c) || Is.tab(c) ){
                i++;
            }

            //String
            else if( Is.quote(c) ){
                i++;
                c = characters[i];

                while( Is.characterOrDigit(c) || c == SPACE){
                    if( Is.quote(c) ){
                        tokenStream.addToken(new Token(token.toString(), Type.STRING));
                    }
                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new SyntaxError(new Token("", Type.STRING),
                            "missing/ unintentional quotation mark");
                    }
                    c = characters[i];
                }
            }

            // ID or Keyword
            else if( Is.character(c) ){
                while( Is.characterOrDigit(c) ){
                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new SyntaxError(new Token("", Type.END_STMT), "missing semi-colon");
                    }
                    c = characters[i];
                }
                tokenStream.addToken(new Token(token.toString(), Type.ID_OR_KEYWORD));
            }

            // Number
            else if( Is.period(c) || Is.digit(c) ){
                boolean hasPeriod = Is.period(c);

                while( Is.period(c) || Is.digit(c) ){
                    if( Is.period(c) ){
                        if( hasPeriod ){
                            throw new SyntaxError(new Token("", Type.NUMBER), "To many decimal places");
                        }
                        hasPeriod = true;
                    }

                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new SyntaxError(new Token("", Type.END_STMT), "missing semi-colon");
                    }
                    c = characters[i];
                }

                tokenStream.addToken(new Token(token.toString(), Type.NUMBER));
            }

            // Assign
            else if( Is.equal(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.ASSIGN));
                i++;
            }

            // End Stmt
            else if( Is.semicolon(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.END_STMT));
                i++;
            }

            // Start Paren
            else if( Is.startParen(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.START_PAREN));
                i++;
            }

            // End Paren
            else if( Is.endParen(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.END_PAREN));
                i++;
            }

            // Power
            else if( Is.carrot(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.POWER));
                i++;
            }

            // Division
            else if( Is.div(c) || Is.mult(c) || Is.plus(c) || Is.minus(c) ) {
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Type.MATH_OP));
                i++;
            }

        }
        return tokenStream;
    }

}
