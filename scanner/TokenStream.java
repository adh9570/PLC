package scanner;

import java.util.ArrayList;
import java.util.List;

/**
 * TokenStream Class
 *
 * This class is used to create an easy to use stream for
 *  the grammar to use. Makes use of {@link Token} to
 *  hold data within the stream.
 */
public class TokenStream {

    /**
     * tokens
     *
     * List of all tokens within the stream.
     */
    private List<Token> tokens;

    /**
     * index
     *
     * Current index that the stream is at.
     */
    private int index;


    /**
     * Constructor()
     */
    TokenStream(){
        tokens = new ArrayList<>();
        index = 0;
    }


    /**
     * addToken(Token)
     *
     * @param token {@link Token}
     */
    void addToken(Token token){
        tokens.add(token);
    }


    /**
     * getNextToken()
     *
     * @return token {@link Token}
     */
    public Token getNextToken(){
        Token t = tokens.get(index);
        index++;
        return t;
    }


    /**
     * peekNextToken()
     *
     * @return token {@link Token}
     */
    public Token peekNextToken(){
        return tokens.get(index);
    }

    /**
     * isEmpty()
     *
     * @return boolean state of where the stream
     *  is at, if end or not.
     */
    public boolean isEmpty() {
        return index + 1 >= tokens.size();
    }


    /**
     * subtractFromIndex
     *
     * @param i int value to move pointer back
     */
    public void subtractFromIndex(int i){
        index = index - i;
    }
}
