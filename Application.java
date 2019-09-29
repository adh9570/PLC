import Scanner.*;

public class Application{
    public static void main(String[]args){
        String str = "Integer x = 5;\nprint( x + 3.2 );";
        Scanner scanner = new Scanner(str);
        TokenStream tokenStream = scanner.scan();

        for( Token token : tokenStream.getTokens() ){
            System.out.println(token);
        }
    }
}