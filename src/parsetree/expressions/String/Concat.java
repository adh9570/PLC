package parsetree.expressions.String;


import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarValue;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.STRING;

class Concat implements GrammarValue {

    private GrammarValue obj1;
    private GrammarValue obj2;

    Concat(StringExpression parent, TokenStream tokenStream) throws SyntaxError {

        // First Parenthesis
        Token openParen = tokenStream.getNextToken();
        if( openParen.getType() != Token.Type.START_PAREN ){
            throw new SyntaxError(openParen, "Missing opening parenthesis for concat");
        }

        obj1 = new StringExpression(parent, tokenStream);

        Token comma = tokenStream.getNextToken();
        if( comma.getType() != Token.Type.COMMA ){
            throw new SyntaxError(comma, "Expected , got " + comma.getValue());
        }

        obj2 = new StringExpression(parent, tokenStream);

        // Second Parenthesis
        Token closeParen = tokenStream.getNextToken();
        if( closeParen.getType() != Token.Type.END_PAREN ){
            throw new SyntaxError(closeParen, "Expected ) got" + closeParen.getValue());
        }
    }


    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public String getType() {
        return STRING;
    }

    @Override
    public Object getValue() throws RuntimeError {
        String val1 = obj1.getValue().toString();
        String val2 = obj2.getValue().toString();

        return val1 + val2;
    }
}
