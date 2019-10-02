package parsetree;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.expressions.Expression;
import scanner.Token;
import scanner.TokenStream;

import static java.lang.System.*;

/**
 * Print ( Class )
 * Extends {@link GrammarObject}
 *
 * This class is used to hold the print
 *  nodes of the parse tree.
 */
class Print extends GrammarObject {


    /**
     * Constructor(GrammarObject, TokenStream)
     *
     * @param parent - Parent object, should be a Program.
     * @param tokenStream - Stream of tokens with current index.
     * @throws SyntaxError - Any errors that my have occurred in parsing.
     */
    Print(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        Token openParen = tokenStream.getNextToken();
        //noinspection EqualsBetweenInconvertibleTypes
        if( !openParen.equals("(") ){
            throw new SyntaxError(openParen, "Print statement missing opening parenthesis");
        }

        new Expression(this, tokenStream);

        Token closedParen = tokenStream.getNextToken();
        //noinspection EqualsBetweenInconvertibleTypes
        if( !closedParen.equals(")") ){
            throw new SyntaxError(closedParen, "Print statement missing closing parenthesis");
        }

    }

    /**
     * run()
     *
     * Prints the value of the child.
     */
    @Override
    public void run() throws RuntimeError {
        GrammarObject child = getChildren().get(0);
        out.println(((Expression) child).getValue());
    }
}
