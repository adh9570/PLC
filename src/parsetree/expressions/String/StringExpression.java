package parsetree.expressions.String;

import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarObject;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.STRING;

public class StringExpression extends GrammarObject implements GrammarValue {

    private Object obj;

    /**
     * Expression(GrammarObject, TokenStream)
     *
     * @param parent - Parent {@link GrammarObject}
     * @param tokenStream - TokenStream at current index
     */
    public StringExpression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);
        Token token = tokenStream.getNextToken();

        if( token.getType() == Token.Type.STRING ){
            obj = token.getValue();
        }
        else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("concat")) {
            obj = new Concat(this, tokenStream);
        }
        else if( token.getType() == Token.Type.ID_OR_KEYWORD && token.getValue().equals("charAt")){
            obj = new CharAt(this, tokenStream);
        }
        else{
            obj = getScope(token.getValue());
        }
    }



    /**
     * getIdentifier()
     *
     * @return null
     */
    @Override
    public String getIdentifier() {
        return null;
    }


    /**
     * getType()
     * @return type - String
     */
    @Override
    public String getType() {
        return STRING;
    }

    /**
     * getValue()
     *
     * @return value as the corresponding type
     */
    @Override
    public Object getValue() throws RuntimeError {
        if( obj instanceof GrammarValue ){
            return ((GrammarValue)obj).getValue();
        }
        return obj;
    }


}
