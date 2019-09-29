package Scanner;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {

    private List<Token> tokens;

    TokenStream(){
        tokens = new ArrayList<Token>();
    }

    void addToken(Token token){
        tokens.add(token);
    }

    public Token[] getTokens() {
        return tokens.toArray(new Token[0]);
    }
}
