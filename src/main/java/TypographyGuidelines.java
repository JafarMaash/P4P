/**
* This class will check identifiers against typography ("the style and appearance of printed matter")
* guidelines in the literature,i.e. casing, underscores, etc.
*
* The current plan is each method loops through the identifiers and checks them against one guideline.
* This should probably be refactored to one method that checks all guidelines in a single for loop,
* to reduce iterations and improve performance.
*
* A few other typographic rules are more comparative (e.g. two identifiers should not differ only by a number).
* I'll implement these later, as I imagine their operations will be a bit more expensive.
* */

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class TypographyGuidelines {

    private Map<String, Map.Entry<Type, Modifier>> identifiers;
    private Map<String,Type> methods;

    private HashSet<String> dictionary;

    public TypographyGuidelines(Map<String, Entry<Type, Modifier>> identifiers, Map<String, Type> methods, HashSet<String> dictionary){
        this.identifiers = identifiers;
        this.methods = methods;
        this.dictionary = dictionary;
    }

    /**
    * Checks Relf's guideleines (2004):
    * no outside underscore characters (e.g. _Apple_Count)
    * no multiple underscore characters (e.g. Apple__Count)
    *
    * Also checks Java docs guidelines for constant/final variable naming (should be e.g. APPLE_COUNT)
    * */
    public int checkUnderscores(){
        int numViolations = 0;
        for(Entry<String, Entry<Type, Modifier>> entry: identifiers.entrySet()){
            if (entry.getValue().getValue() == Modifier.finalModifier()){ //
                numViolations += followsConstantTypography(entry.getKey()) ? 0 : 1;
                // java docs guidelines for constants/final variables
            }
            char[] charArray = entry.getKey().toCharArray();
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

    /**
    * Java specification guideline
    * The names of constants in interface types should be, and final variables of class types
    * may conventionally be, a sequence of one or more words, acronyms, or abbreviations,
    * all uppercase, with components separated by underscore "_" characters
    * */
    private boolean followsConstantTypography(String identifier) {
        String constantPattern = "^[A-Z]+(?:_[A-Z]+)*$";
        return identifier.matches(constantPattern);
    }

    /**
    * Per the Java spec
    * Names of fields that are not final (and methods) should be in mixed case with a lowercase first letter
    * and the first letters of subsequent words capitalized.
    * */
    public int checkCamelCase(){
        int numViolations = 0;
        for(Entry<String, Entry<Type, Modifier>> entry: identifiers.entrySet()){
            if (entry.getValue().getValue() == Modifier.finalModifier()){ // if identifier is final, does not need to follow camelcase
                // Constant typography violation is checked for in checkUnderscores, so no need to double count them in the line below.
                // numViolations += followsConstantTypography(entry.getKey()) ? 0 : 1;
                continue;
            } else {
                //TODO: split camel case, if isCamelCase returns false then attempt to tokenize, if more than 1 word is outputted then add violation.
                numViolations += isCamelCase(entry.getKey()) ? 0 : 1;
                if (!isCamelCase(entry.getKey())) {
                    //TODO: call recursive methods for prefix/suffix, if it tokenizes then add violation
                }
            }
        }
        //TODO: apply same logic from identifiers
        for(String method : methods.keySet()){
            numViolations += isCamelCase(method) ? 0 : 1;
            if (!isCamelCase(method)) {
//                System.out.println("entry key: " + method);
            }
        }
        return numViolations;
    }

    /**
     * Helper function for checkCamelCase()
     */
    private boolean isCamelCase(String identifier){
        String camelCasePattern = "([a-z]+[A-Z]+\\w+)+";
        return identifier.matches(camelCasePattern);
    }

    /**
     * Per Relf's guidelines (2004)
     * [No] Long Identifier Names i.e., an identifier name longer than twenty characters
     * */
    public int longerThanTwentyCharacters(){
        int numViolations = 0;
        for(String method : methods.keySet()){
            numViolations += (method.length() <= 20) ? 0 : 1;
        }

        for(String identifier : identifiers.keySet()){
            numViolations += (identifier.length() <= 20) ? 0 : 1;
        }
        return numViolations;
    }

    /**
     * Check prefix
     */
}
