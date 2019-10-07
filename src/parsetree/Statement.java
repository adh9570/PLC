package parsetree;

import errors.SyntaxError;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.*;

/**
 * Statement Class
 * Extends {@link GrammarObject}
 *
 * This class is used to hold the statements
 *  withing the parse tree.
 */
class Statement extends GrammarObject {

    // Key Words or Reserved Words
    private static final String PRINT = "print";


    /**
     * Constructor(GrammarObject, TokenStream)
     *
     * @param parent - Parent object, should be a Program.
     * @param tokenStream - Stream of tokens with current index.
     * @throws SyntaxError - Any errors that my have occurred in parsing.
     */
    Statement(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        Token nextToken = tokenStream.getNextToken();
        String token = nextToken.toString();
        if( nextToken.getType() == Token.Type.END_STMT ){
            return;
        }


        // Print Option
        if( token.equals(PRINT)){
            new Print(this, tokenStream);
        }

        else if( token.equals(INTEGER) || token.equals(DOUBLE) ||
            token.equals(STRING))
        {
            new Assignment(this, tokenStream);
        }


        // No SemiColon
        Token endStmt = tokenStream.getNextToken();
        if( endStmt.getType() != Token.Type.END_STMT ){
            throw new SyntaxError(endStmt, "Missing semicolon");
        }
    }

}
