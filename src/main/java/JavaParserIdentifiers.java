import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Here's the class for extracting identifiers from classes.
 * It gets all the variable and method names from a repository, and stores them in two hashmaps respectively,
 * along with their type/return type.
 * Then it calls a guideline checker (TypographyGuidelines), which returns the number of violations for each rule.
 *
 * If there's something else you wanna do with the parser (like maybe extract class names and apply rules for those too)
 * and it's not already in this class, have a look at Ewan's example repo on Github - link can be found in the readme.
 * This class is a copy of that, but with extra parser functionalities you might find useful.
 *
 * Also I'm going to leave in any print statements, because you may find them useful when trying to understand
 * what's going on.
 *
 * */
public class JavaParserIdentifiers {
    public static void main(String[] args) throws Exception {
        int numViolations = 0;
        int[] overTwenty; // [num fields > 20 chars, num methods > 20 chars]
        String rootPath = "test_repos";
        String src1 = rootPath + "/pixel-dungeon-master"; // insert repo path here
        Analyser analyser = new Analyser();
        analyser.addSourcePath(src1);
        Modifier FINAL = Modifier.finalModifier();
        Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(src1);
        Map<String, Entry<Type, Modifier>> fields = new HashMap<>(); // key = field name, value = new entry(key = field type, value = final modifier or null)
        Map<String, Type> methods = new HashMap<>(); // key = method name, value = return type

        // go through every file in the repo, and add identifiers to the hashmap
        for (Path path : paths) {
            try{
            CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
            VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(VariableDeclarator n, Object arg) {
                    super.visit(n, arg);
                }

                // Add fields
                @Override
                public void visit(VariableDeclarationExpr n, Object arg) {
                    super.visit(n, arg);
                    VariableDeclarator vd = n.getVariable(0);
                    fields.put(vd.getNameAsString(), new SimpleEntry<>(vd.getType(), null));
                }

                // Add fields with FINAL modifiers as well
                @Override
                public void visit(FieldDeclaration n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println("VARIABLE DECLARATOR EXPRESSION");
//                    System.out.println("expr: " + n.toString());
//                    System.out.println("variables: " + n.getVariables());
                    VariableDeclarator vd = n.getVariable(0);
//                    System.out.println("modifiers: " + n.getModifiers());
                    Modifier mod = null;
                    if (n.getModifiers().contains(FINAL)) {
                        mod = FINAL;
                    }
                    fields.put(vd.getNameAsString(), new SimpleEntry<>(vd.getType(), mod));
//                    System.out.println("IDENTIFIER NAME " + vd.getNameAsString());
//                    System.out.println("IDENTIFIER TYPE " + vd.getType());
                }

                // Add methods
                @Override
                public void visit(MethodDeclaration md, Object arg) {
                    super.visit(md, arg);
//                    System.out.println("METHOD NAME " + md.getName());
//                    System.out.println("METHOD TYPE " + md.getType());
                    methods.put(md.getNameAsString(), md.getType());
                }

                @Override
                public void visit(PrimitiveType n, Object arg) {
                    super.visit(n, arg);
                }

                @Override
                public void visit(ClassOrInterfaceType n, Object arg) {
                    super.visit(n, arg);
                }
            };
//            System.out.println("MODULE for " + path);
            // TODO: could create a new IdentifierInfo object here, pass it to the visitor as the 2nd arg instead of null
            visitor.visit(compilationUnit, null);

        }
            catch(Exception e){
            System.out.println(path);
        }

        }
        TypographyGuidelines typographyGuidelines = new TypographyGuidelines(fields, methods);
        numViolations += typographyGuidelines.checkUnderscores();
        String caseCheckResults = typographyGuidelines.checkCaseTypes();
        overTwenty = typographyGuidelines.longerThanTwentyCharacters();
        System.out.println("underscore violations: " + numViolations);
        System.out.println("Number of fields: " + fields.size());
        System.out.println("Long fields " + overTwenty[0]);
        System.out.println("Number of methods: " + methods.size());
        System.out.println("Long methods " + overTwenty[1]);
        System.out.println(caseCheckResults);


        // Write the results to a separate (temporary/burner) csv, which makes for easy copy pasting into the actual spreadsheet of data.
        // TODO: you could probably tidy up the csv writing/formatting to write straight into an actual data spreadsheet instead of a temporary one.
        File file = new File("python_parsing/csv_data.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "Repo name" };
            writer.writeNext(new String[]{src1});

            // add data to csv
            String[] data1 = { "Total methods", String.valueOf(methods.size())};
            writer.writeNext(data1);
            String[] data2 = { "Method violations (length)", String.valueOf(overTwenty[1]) };
            writer.writeNext(data2);
            String[] data3 = { "Method violations (case)", "insert here after running Python script" };
            writer.writeNext(data3);
            String[] data4 = { "Total fields", String.valueOf(fields.size())};
            writer.writeNext(data4);
            String[] data5 = { "Field violations (length)", String.valueOf(overTwenty[0]) };
            writer.writeNext(data5);
            String[] data6 = { "Field violations (case)", "insert here after running Python script" };
            writer.writeNext(data6);
            String[] data7 = { "Primary case type", "Insert here" };
            writer.writeNext(data7);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
