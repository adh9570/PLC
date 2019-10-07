package driver;

import errors.NoFileFoundError;
import io.FileIn;
import parsetree.Program;
import scanner.Scanner;
import scanner.TokenStream;

import static java.lang.System.*;
import static java.lang.System.err;

public class Application{

    public static String FILE_NAME;
    public static TokenStream TOKEN_STREAM;
    public static Scanner SCANNER;
    private static final String VERSION = "1.0";


    public static void main(String[]args){
        if(args[0].equalsIgnoreCase("-v") || args[0].equalsIgnoreCase("-version")){
            out.println("Jott version " + VERSION);
        }
        else if(args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help")){
            out.println("Usage: Jott [-options] jottfile [args...]");
        }

        // Get Input
        String input = "";
        try {
            input = new FileIn(args[0]).read();
            FILE_NAME = args[0];
        } catch (NoFileFoundError e) {
            err.println(e.getMessage());
        }

        // Scan Code
        SCANNER = new Scanner(input);

        // Parse Code

        // Run Code
        try {
            TOKEN_STREAM = SCANNER.scan();
            Program program = new Program(TOKEN_STREAM);
            program.run();
        }
        catch (Exception e){
            err.println(e.getMessage());
        }

    }
}