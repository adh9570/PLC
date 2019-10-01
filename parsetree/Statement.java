package parsetree;

import errors.SyntaxError;
import scanner.Token;
import scanner.TokenStream;

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
    private static final String INTEGER = "Integer";


    /**
     * Constructor(GrammarObject, TokenStream)
     *
     * @param parent - Parent object, should be a Program.
     * @param tokenStream - Stream of tokens with current index.
     * @throws Exception - Any errors that my have occurred in parsing.
     */
    Statement(GrammarObject parent, TokenStream tokenStream) throws Exception {
        super(parent);

        Token nextToken = tokenStream.getNextToken();

        // Print Option
        if(nextToken.equals(PRINT)){
            new Print(this, tokenStream);
        }


        // No SemiColon
        Token endStmt = tokenStream.getNextToken();
        if( endStmt.getType() != Token.Type.END_STMT ){
            throw new SyntaxError(endStmt, "Missing semicolon");
        }
    }

}
