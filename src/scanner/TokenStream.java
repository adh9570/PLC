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
     * rawFile
     *
     * Raw Text File of input
     */
    private String rawFile;


    /**
     * Constructor()
     */
    TokenStream(String fileData){
        tokens = new ArrayList<>();
        index = 0;
        rawFile = fileData;
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
        if(index >= tokens.size()){
            return null;
        }
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
     * peekNextToken()
     *
     * @return token {@link Token}
     */
    public Token peekNextToken(int i){
        if(index + (i- 1) >= tokens.size()){
            return null;
        }
        return tokens.get(index + (i- 1));
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


    public String getLine(int line) {
        String lineData = rawFile.split("\n")[line - 1];
        return lineData.replace("\r", "");
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
