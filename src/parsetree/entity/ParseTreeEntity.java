package parsetree.entity;

import driver.JottError;

interface ParseTreeEntity {

    void construct() throws JottError.JottException;

    void establish()  throws JottError.JottException ;

    boolean isValid();

    void invalidate();

    Class getType();

    void reset();
}
