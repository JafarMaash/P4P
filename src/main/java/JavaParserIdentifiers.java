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

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Here's the class for extracting identifiers from classes.
 * Right now it only gets variable and method names from one class, and stores them in separate hashmaps,
 * along with their variable/method return type.
 * Then it calls a guideline checker, which returns the number of violations (no further details/info on the violations,
 * but could easily add that).
 *
 * Next steps here are to expand the extraction from one class out to a whole app, and other stuff that will come to mind
 * as we continue to work on this.
 * Also need to get rid of all the commented out unused stuff in here, will do that later when it becomes more clear what we need
 * and don't need.
 * */
public class JavaParserIdentifiers {
    public static void main(String[] args) throws Exception {
        int numViolations = 0;
        int numCamel = 0;
        int[] overTwenty;
        String rootPath = "test_repos";
        String src1 = rootPath + "/design1000/";
        Analyser analyser = new Analyser();
        analyser.addSourcePath(src1);
        Modifier FINAL = Modifier.finalModifier();
        Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(src1);
        Map<String, Entry<Type, Modifier>> identifiers = new HashMap<>();
        Map<String, Type> methods = new HashMap<>(); // key method name, value return type

        HashSet<String> dict = null;
        for (Path path : paths) {
            CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
            VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(VariableDeclarator n, Object arg) {
                    super.visit(n, arg);
                }

                @Override
                public void visit(VariableDeclarationExpr n, Object arg) {
                    super.visit(n, arg);
                    VariableDeclarator vd = n.getVariable(0);
                    identifiers.put(vd.getNameAsString(), new SimpleEntry<>(vd.getType(), null));
                }

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
                    identifiers.put(vd.getNameAsString(), new SimpleEntry<>(vd.getType(), mod));
//                    System.out.println("IDENTIFIER NAME " + vd.getNameAsString());
//                    System.out.println("IDENTIFIER TYPE " + vd.getType());
                }

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
            //todo create a new identifierinfo object here, pass it to the visitor as the 2nd arg instead of null
            visitor.visit(compilationUnit, null);

            dict = new HashSet();
            BufferedReader br = new BufferedReader(new FileReader("src/main/dictionaries/en_US.dic"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("/")) {
                    dict.add(line.split("/")[0]);
                } else {
                    dict.add(line);
                }
            }

        }
        TypographyGuidelines typographyGuidelines = new TypographyGuidelines(identifiers, methods, dict);
        // todo put all the identifiers/methods into one big txt, to pass into python and check for PoS
        numViolations += typographyGuidelines.checkUnderscores();
        String caseCheckResults = typographyGuidelines.checkCaseTypes();
        overTwenty = typographyGuidelines.longerThanTwentyCharacters();
        System.out.println("underscores: " + numViolations);
        System.out.println("Number of identifiers: " + identifiers.size());
        System.out.println("Long identifiers " + overTwenty[0]);
        System.out.println("Number of methods: " + methods.size());
        System.out.println("Long methods " + overTwenty[1]);
        System.out.println(caseCheckResults);
    }
}
