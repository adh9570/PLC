package driver;

import parsetree.Program;
import scanner.Scanner;
import scanner.TokenStream;

import static java.lang.System.out;

public class Application {

    private static void usage(){
        out.println("Usage: Jott [-options] jottfile [args...]");
        System.exit(0);
    }

    private static void runFile(String filename) throws JottError.JottException {
        TokenStream tokenStream;
        JottError error = new JottError();

        // Get Input
        String input = new FileIn(filename).read();
        error.setFilename(filename);
        if(input == null)
            error.throwFileNotFound();

        // Scan Code
        Scanner scanner = new Scanner(input, error);

        // Parse Code
        tokenStream = scanner.scan();
        error.setTokenStream(tokenStream);
        Program program = new Program(tokenStream, error);

        // Run Code
        program.execute();

    }

    public static void main(String[] args) {
        if(args.length == 0)
            usage();
        if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help"))
            usage();

        try {
            runFile(args[0]);
        }
        catch (JottError.JottException ignored) {
            // Ignored
        }

    }

}
