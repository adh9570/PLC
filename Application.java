import errors.NoFileFoundError;
import io.FileIn;
import parsetree.Program;
import scanner.*;

import static java.lang.System.*;

public class Application{
    public static void main(String[]args){

        // Get Input
        String input = "";
        if(args[0].equals("TEST1")){
            input = "print( 10.5 + 3.5 );";
        }
        else{
            try {
                input = new FileIn(args[0]).read();
            } catch (NoFileFoundError e) {
                err.println(e.getMessage());
            }
        }

        // Scan Code
        Scanner scanner = new Scanner(input);

        // Parse Code
        TokenStream tokenStream = scanner.scan();

        // Run Code
        try {
            Program program = new Program(tokenStream);
            program.run();
        }
        catch (Exception e){
            err.println(e.getMessage());
        }

    }
}