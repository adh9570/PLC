package driver;

import scanner.Token;
import scanner.TokenStream;

import static java.lang.System.out;

public class JottError {

    private TokenStream tokenStream;
    private String filename;

    public static class JottException extends Exception{
        public JottException(){
            super();
        }
    }

    void setFilename(String filename){
        this.filename = filename;
    }

    public void setTokenStream(TokenStream tokenStream){
        this.tokenStream = tokenStream;
    }

    public void throwSyntax(String message, Token token) throws JottException {
        out.print("Syntax Error: " + message);
        out.print(", \"");
        out.print(tokenStream.getLine(token.getLine()));
        out.println("\" " +"(inputs/" + filename + ":" + token.getLine() + ")");
        throw new JottException();
    }

    public void throwSyntax(String message) throws JottException {
        String errorMessage = "Syntax Error: " + message;
        out.println(errorMessage);
        throw new JottException();
    }

    void throwFileNotFound() throws JottException {
        String errorMessage = "No file found by the name '" + filename + "'";
        out.println(errorMessage);
        throw new JottException();
    }


    public void throwRuntime(String message, Token token) throws JottException {
        String errorMessage = "Runtime Error: " +
                message +
                ", \"" + tokenStream.getLine(token.getLine()) + "\" " +
                "(inputs/" + filename + ":" + token.getLine() + ")";
        out.println(errorMessage);
        throw new JottException();
    }

}
