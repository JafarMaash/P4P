/**
* This class will check identifiers against typography ("the style and appearance of printed matter")
* guidelines in the literature,i.e. casing, underscores, etc.
*
* The current plan is each method loops through the identifiers and checks them against one guideline.
* This should probably be refactored to one method that checks all guidelines in a single for loop,
* to reduce iterations and improve performance.
*
* A few other typographic rules are more comparative (e.g. two identifiers should not differ only by a number).
* These have not been implemented in the interest of time and efficiency, but are a future work point to consider.
 *
 * TODO: could add numViolations into an IdentifierInfo class, and play around with it in there.
* */

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.type.Type;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public class TypographyGuidelines {

    private Map<String, Map.Entry<Type, Modifier>> fields;
    private Map<String,Type> methods;

    private int camelCaseUses = 0;
    private int pascalCaseUses = 0;
    private int snakeCaseUses = 0;
    private int kebabCaseUses = 0;

    public TypographyGuidelines(Map<String, Entry<Type, Modifier>> fields, Map<String, Type> methods){
        this.fields = fields;
        this.methods = methods;

    }

    /**
    * Checks Relf's guidelines (2004):
    * no outside underscore characters (e.g. _Apple_Count)
    * no multiple underscore characters (e.g. Apple__Count)
    *
    * Also checks Java docs guidelines for constant/final variable naming (should be e.g. APPLE_COUNT)
    * */
    public int checkUnderscores(){
        int numViolations = 0;
        for(Entry<String, Entry<Type, Modifier>> entry: fields.entrySet()){

            // Check if it's final, and if so follows final field rules
            if (entry.getValue().getValue() == Modifier.finalModifier()){
                numViolations += followsConstantTypography(entry.getKey()) ? 0 : 1;
                // Java Language Spec guideline for constants/final variables
            }

            char[] charArray = entry.getKey().toCharArray();

            // Outside underscores
            if (Arrays.asList(charArray[charArray.length - 1], charArray[0]).contains('_')){
                numViolations++;
                System.out.println("underscore vio: " + entry.getKey());
                continue;
            }

            // Multiple i.e contiguous underscores
            for (int i = 0; i < charArray.length; i++){
                try {
                    if (charArray[i] == '_' && charArray[i + 1] == '_') {
                        numViolations++;
                        System.out.println("underscore vio: " + entry.getKey());
                        //TODO: write all these violations to different folders to later examine?
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                    System.out.println("exception: " + entry.getKey());
                }
            }
        }
        return numViolations;
    }

    /**
    * Checks a Java Language Specification guideline
    * This method is currently called from inside checkUnderscores()
    * "The names of constants in interface types should be, and final variables of class types
    * may conventionally be, a sequence of one or more words, acronyms, or abbreviations,
    * all uppercase, with components separated by underscore "_" characters"
    * */
    private boolean followsConstantTypography(String identifier) {
        String constantPattern = "^[A-Z]+(?:_[A-Z]+)*$";
        return identifier.matches(constantPattern);
    }

    /**
     * Check if identifiers follow a casing convention; if they don't, write them to a txt file to later split and check.
     * Also add all identifiers to a separate txt file for part of speech rules later.
    * */
    public String checkCaseTypes() throws Exception {

        // Set up text file for casing violations
        File caseViolationsFile = new File("python_parsing/toSplit.txt");
        if(!(caseViolationsFile.exists() && !caseViolationsFile.isDirectory())) {
            FileWriter fw = new FileWriter("python_parsing/toSplit.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        }

        // Set up text file for all identifiers, to later be checked for part of speech rules
        File allIdsFile = new File("python_parsing/allIdentifiers.txt");
        if(!(allIdsFile.exists() && !allIdsFile.isDirectory())) {
            FileWriter fw = new FileWriter("python_parsing/allIdentifiers.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        }

        // For every field
        for(Entry<String, Entry<Type, Modifier>> entry: fields.entrySet()){

            // Add every field to allIdentifiers.txt, along with its type and whether or not it's final
            writeFieldToFile(allIdsFile, entry);

            // Add fields which don't superficially follow a casing convention to caseViolationsFile, to be evaluated later in splitter.py
            if (entry.getValue().getValue() != null){ // if identifier is final
                // Constant (i.e. final fields) typography violation is checked for in checkUnderscores,
                // so we can skip checking them here.
                continue;
            } else { // if field is not final
                if (!followsCasingConvention(entry.getKey())) {
                    // write to caseViolationsFile if it doesn't appear to follow a casing convention
                    writeFieldToFile(caseViolationsFile, entry);
                }
            }
        }

        // For every method
        for(Entry<String, Type> method : methods.entrySet()){

            // Add every field to allIdentifiers.txt, along with its return type
            writeMethodToTextFile(allIdsFile, method);

            // Add fields which don't superficially follow a casing convention to caseViolationsFile, to be evaluated later in splitter.py
            if (!followsCasingConvention(method.getKey())) {
                writeMethodToTextFile(caseViolationsFile, method);
            }
        }
        return "camel case usages: " + camelCaseUses + "\npascal case usages: " + pascalCaseUses + "\nsnake case usages: " + snakeCaseUses + "\nkebab case usages: " + kebabCaseUses;
    }

    private void writeMethodToTextFile(File fileToWriteTo, Entry<String, Type> method) {
        FileWriter fw1;
        try {
            // every identifier gets written to allIdentifiers.txt before we check if it follows any guidelines
            fw1 = new FileWriter(fileToWriteTo, true);
            BufferedWriter br = new BufferedWriter(fw1);
            br.write("METHOD" + "\t" + method.getKey() + "\t" + method.getValue() + "\n"); // key = method name, value = return type
            br.close();
            fw1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFieldToFile(File fileToWriteTo, Entry<String, Entry<Type, Modifier>> entry) {
        FileWriter fw;
        try {
            fw = new FileWriter(fileToWriteTo, true);
            BufferedWriter br = new BufferedWriter(fw);
            br.write("FIELD" + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");
            br.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper functions for checkCaseTypes()
     */
    private boolean isCamelCase(String identifier){
        String camelCasePattern = "([a-z]+[A-Z]+\\w+)+";
        return identifier.matches(camelCasePattern);
    }

    private boolean isPascalCase(String identifier) {
        String pascalCase = "(?:[A-Z][a-z0-9]+)(?:[A-Z]+[a-z0-9]*)*";
        return identifier.matches(pascalCase);
    }

    private boolean isSnakeCase(String identifier) {
        String snakeCasePattern = "^(?:[a-z]++_)*+[a-z]++$";
        return identifier.matches(snakeCasePattern);
    }

    private boolean isKebabCase(String identifier) {
        String kebabCasePattern = "[a-z0-9]+(?:-[a-z0-9]+)*";
        return identifier.matches(kebabCasePattern);
    }


    /**
     * Per Relf's guidelines (2004)
     * [No] Long Identifier Names i.e., an identifier name longer than twenty characters
     * Probably won't check for short identifier names (at least for now), as this is debated in the literature.
     * */
    public int[] longerThanTwentyCharacters(){
        int methodViolations = 0;
        int fieldViolations = 0;
        for(String methodName : methods.keySet()){
            methodViolations += (methodName.length() <= 20) ? 0 : 1;
        }

        for(String fieldName : fields.keySet()){
            fieldViolations += (fieldName.length() <= 20) ? 0 : 1;
        }
        return new int[] {fieldViolations, methodViolations };
    }

    /**
     * Method to check for different case types
     * @param identifier the identifier to be checked
     * @return true if it matches a naming guideline, false if not
     */
    private boolean followsCasingConvention(String identifier) {
        if (isCamelCase(identifier)) {
            camelCaseUses += 1;
            return true;
        } else if (isPascalCase(identifier)) {
            pascalCaseUses += 1;
            return true;
        } else if (isSnakeCase(identifier) && identifier.contains("_")) {
            snakeCaseUses += 1;
            return true;
        } else if (isKebabCase(identifier) && identifier.contains("-")) {
            kebabCaseUses += 1;
            return true;
        } else {
            return false;
        }
    }
}
