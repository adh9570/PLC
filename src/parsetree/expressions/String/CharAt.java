package parsetree.expressions.String;


import errors.RuntimeError;
import errors.SyntaxError;
import parsetree.GrammarValue;
import parsetree.expressions.Integer.IntegerExpression;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.STRING;

class CharAt implements GrammarValue {

    private GrammarValue obj1;
    private GrammarValue obj2;

    CharAt(StringExpression parent, TokenStream tokenStream) throws SyntaxError {

        // First Parenthesis
        Token openParen = tokenStream.getNextToken();
        if( openParen.getType() != Token.Type.START_PAREN ){
            throw new SyntaxError(openParen, "Missing opening parenthesis for charAt");
        }

        obj1 = new StringExpression(parent, tokenStream);

        Token comma = tokenStream.getNextToken();
        if( comma.getType() != Token.Type.COMMA ){
            throw new SyntaxError(comma, "Missing parameter for charAt");
        }

        obj2 = new IntegerExpression(parent, tokenStream);

        // Second Parenthesis
        Token closeParen = tokenStream.getNextToken();
        if( closeParen.getType() != Token.Type.END_PAREN ){
            throw new SyntaxError(closeParen, "Missing closing parenthesis for charAt");
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
        int val2 = Integer.parseInt(obj2.getValue().toString());

        return val1.charAt(val2);
    }
}