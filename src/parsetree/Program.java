package parsetree;

import scanner.TokenStream;

/**
 * Program ( Class )
 * Extends: {@link GrammarObject}
 *
 * Program is the root node to the program.
 *  This class constructs the whole parse tree given
 *  a token stream.
 */
public class Program extends GrammarObject {

    public Program(TokenStream tokenStream) throws Exception{
        super(null);

        while(!tokenStream.isEmpty()){
            new Statement(this, tokenStream);
        }
    }

}
