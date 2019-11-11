package driver;

import java.util.Arrays;
import java.util.List;

public class ReservedWords {

    private ReservedWords(){
        // Ignored
    }

    private static final List<String> RESERVED = Arrays.asList(
            "concat",
            "charAt",
            "print",
            "if",
            "while",
            "void",
            "String",
            "Integer",
            "Double"
    );

    public static boolean isReserved(String str){
        return RESERVED.contains(str);
    }
}
