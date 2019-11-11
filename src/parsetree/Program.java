package parsetree;

import driver.JottError;
import parsetree.entity.JottEntity;
import scanner.TokenStream;

public class Program extends JottEntity {

    public Program(TokenStream tokenStream, JottError error) throws JottError.JottException {
        super(tokenStream, error);
        initScope();

        while(!tokenStream.isEmpty()){
            JottEntity child = new Statement(this);
            child.construct();
            if(child.isValid())
                child.establish();
        }
    }

}
