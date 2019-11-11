package parsetree.entity;

import driver.JottError;
import scanner.Token;

interface RunTimeEntity {

    void execute() throws JottError.JottException;

    Object getValue() throws JottError.JottException;

    void initScope();

    void scopeVariable(String keyword, JottEntity value);

    JottEntity findInScope(String keyword, Token errorToken) throws JottError.JottException;

    JottEntity findInScope(String keyword);

}
