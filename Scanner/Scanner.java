package Scanner;

import static javax.xml.stream.XMLStreamConstants.SPACE;

public class Scanner {

    private String preScanned;

    public Scanner(String str) {
        preScanned = str;
    }


    public TokenStream scan(){
        try {
            return scan_throws_error();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private TokenStream scan_throws_error() throws Exception{

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
                        tokenStream.addToken(new Token(token.toString(), Token.Type.STRING));
                    }
                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new Exception("Syntax Error, missing/ unintentional quotation mark");
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
                        throw new Exception("Syntax Error, missing semi-colon");
                    }
                    c = characters[i];
                }
                tokenStream.addToken(new Token(token.toString(), Token.Type.ID_OR_KEYWORD));
            }

            // Number
            else if( Is.period(c) || Is.digit(c) ){
                boolean hasPeriod = Is.period(c);

                while( Is.period(c) || Is.digit(c) ){
                    if( Is.period(c) ){
                        if( hasPeriod ){
                            throw new Exception("Syntax Error, To many decimal places");
                        }
                        hasPeriod = true;
                    }

                    token.append(c);
                    i++;
                    if( i >= characters.length ){
                        throw new Exception("Syntax Error, missing semi-colon");
                    }
                    c = characters[i];
                }

                tokenStream.addToken(new Token(token.toString(), Token.Type.ID_OR_KEYWORD));
            }

            // Assign
            else if( Is.equal(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.ASSIGN));
                i++;
            }

            // End Stmt
            else if( Is.semicolon(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.END_STMT));
                i++;
            }

            // Start Paren
            else if( Is.startParen(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.START_PAREN));
                i++;
            }

            // End Paren
            else if( Is.endParen(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.END_PAREN));
                i++;
            }

            // Power
            else if( Is.carrot(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.POWER));
                i++;
            }

            // Division
            else if( Is.div(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.DIVIDE));
                i++;
            }

            // Multiplication
            else if( Is.mult(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.MULT));
                i++;
            }

            // Addition
            else if( Is.plus(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.PLUS));
                i++;
            }

            // Subtraction
            else if( Is.minus(c) ){
                token.append(c);
                tokenStream.addToken(new Token(token.toString(), Token.Type.MINUS));
                i++;
            }

        }
        return tokenStream;
    }

}
