package parsetree;

import errors.SyntaxError;
import scanner.Token;
import scanner.TokenStream;

import static parsetree.Constants.*;

/**
 * Expression
 * Extends {@link GrammarObject}
 * Implements {@link GrammarValue}
 */
public class Expression extends GrammarObject implements GrammarValue {

    private Object firstPar;
    private Token operation;
    private Object secondPar;
    private String type = INTEGER;


    /**
     * Expression(GrammarObject, TokenStream)
     *
     * @param parent - Parent {@link GrammarObject}
     * @param tokenStream - TokenStream at current index
     * @throws SyntaxError - Any Syntax error that may occur
     */
    Expression(GrammarObject parent, TokenStream tokenStream) throws SyntaxError {
        super(parent);

        setFirstPar(tokenStream);

        // Operation
        operation = tokenStream.getNextToken();
        if( operation.getType() != Token.Type.MATH_OP ){
            tokenStream.subtractFromIndex(1);
            return;
        }

        setSecondPar(tokenStream);

    }

    /**
     * setFirstPar
     *
     * @param tokenStream - TokenStream at current index
     */
    private void setFirstPar(TokenStream tokenStream){
        Token firstToken = tokenStream.getNextToken();

        if( firstToken.getType() == Token.Type.NUMBER){
            if(firstToken.getValue().contains(".")){
                firstPar = Double.parseDouble(firstToken.getValue());
                type = DOUBLE;
            }
            else {
                firstPar = Integer.parseInt(firstToken.getValue());
            }
        }
        else{
            firstPar = getScope(tokenStream.peekNextToken().getValue());
            type = ((GrammarValue) firstPar).getType();
        }
    }

    /**
     * setSecondPar
     *
     * @param tokenStream - TokenStream at current index
     */
    private void setSecondPar(TokenStream tokenStream) throws SyntaxError {

        Token thirdToken = tokenStream.getNextToken();
        if( thirdToken.getType() == Token.Type.NUMBER){
            if( thirdToken.getValue().contains(".") && type.equals(INTEGER) ){
                tokenStream.subtractFromIndex(3);
                throw new SyntaxError(thirdToken, "Type mismatch");
            }

            if( type.equals(INTEGER) )
                secondPar = Integer.parseInt(thirdToken.getValue());
            else
                secondPar = Double.parseDouble(thirdToken.getValue());
        }
        else{
            secondPar = getScope(thirdToken.getValue());
            if( !((GrammarValue) secondPar).getType().equals(type)){
                throw new SyntaxError(thirdToken, "Type mismatch");
            }
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
        return type;
    }

    /**
     * getValue()
     *
     * @return value as the corresponding type
     */
    @Override
    public Object getValue() {
        if(type.equals(STRING)){
            return null;
        }

        double value = getDoubleValue();
        if( type.equals(INTEGER) ){
            return (int) value;
        }
        return value;
    }


    /**
     * getDoubleValue()
     *
     * @return value as double type
     */
    private double getDoubleValue(){

        //String type
        if(type.equals(STRING)){
            return 0;
        }

        // No operation to be had
        if( secondPar == null ){
            if(firstPar instanceof GrammarValue){
                return (double) ((GrammarValue) firstPar).getValue();
            }
            return (double) firstPar;
        }

        //
        double val1;
        double val2;

        if(firstPar instanceof GrammarValue)
            val1 = Double.parseDouble(((GrammarValue) firstPar).getValue().toString());
        else
            val1 = Double.parseDouble(firstPar.toString());

        if(secondPar instanceof GrammarValue)
            val2 = Double.parseDouble(((GrammarValue) secondPar).getValue().toString());
        else
            val2 = Double.parseDouble(secondPar.toString());

        //Operation
        switch(operation.toString()) {
            case PLUS_OP:
                return val1 + val2;
            case MINUS_OP:
                return val1 - val2;
            case MULT_OP:
                return val1 * val2;
            case DIV_OP:
                return val1 / val2;
            default:
                return 0;
        }
    }

}
