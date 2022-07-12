/*
* This class will check identifiers against typography ("the style and appearance of printed matter")
* guidelines in the literature,i.e. casing, underscores, etc.
* */

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

import java.util.Arrays;
import java.util.Map;

public class TypographyGuidelines {

    private Map<String,Type> identifiers;
    public TypographyGuidelines(Map<String, Type> identifiers){
        this.identifiers = identifiers;
    }

    /*
    * Checks Relf's guideleines (2004):
    * no outside underscore characters (e.g. _Apple_Count)
    * no multiple underscore characters (e.g. Apple__Count)
    * */
    public int checkUnderscores(){
        int numViolations = 0;
        for(String identifier : identifiers.keySet()){
            char[] charArray = identifier.toCharArray();
            if (Arrays.asList(charArray[charArray.length - 1], charArray[0]).contains('_')){ // outside underscores
                numViolations++;
            }
            for (int i = 0; i < charArray.length; i++){
                if(charArray[i] == '_' && charArray[i+1] == '_'){
                    numViolations++; // multiple i.e contiguous underscores
                }
            }
        }
        return numViolations;
    }
}
