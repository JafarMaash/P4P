import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.VariableDeclaratorMetaModel;

import com.github.javaparser.resolution.UnsolvedSymbolException;

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
        String rootPath = "kalah_designs";
        String src1 = rootPath + "/design1041/src/kalah/TestingParser";
        Analyser analyser = new Analyser();
        analyser.addSourcePath(src1);
        Modifier FINAL = Modifier.finalModifier();
        Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(rootPath);
        Map<String, Entry<Type, Modifier>> identifiers = new HashMap<>();
        Map<String, Type> methods = new HashMap<>(); // key method name, value return type

// Put all of that stuff weve found (the maps) in a new class
        // identifierinfo.java
        //

        // Current hacky move: singling out Kalah class from design 1041 to check its variables
        // Prints all identifiers declared inside Kalah.java as of right now
        // Currently commenting out all the visit functionality we don't need: just printing identifier type and name.
        Path[] pathsList = paths.toArray(new Path[paths.size()]);
        Path path = pathsList[23];
//        for (Path path: paths) {
            CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
            VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() { // change <Object> to identifierinfo
                @Override
                public void visit(VariableDeclarator n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println("VariableDeclarator");
//                    SimpleName sn = n.getName();
//                    System.out.println(n.getType() + ": " + n.getName().toString());
//                    Optional<TokenRange> tr = sn.getTokenRange();
//                    identifiers.put( n.getName().toString(), n.getType());
//                    identifiers.put( n.getName().toString(), new AbstractMap.SimpleEntry<>(n.getType(), ));
//                    System.out.println(n.getParentNode());
//                    System.out.println("Type: "+ n.getType());
//                    System.out.println(" - " + n);
//                    if (n.resolve().isType()) {
//                        System.out.println("   FQN (type):" + n.resolve().getType());
//                    } else {
//                        System.out.println("   FQN (not type):" + n.resolve().getName());
//                    }
                }

                @Override
                public void visit(VariableDeclarationExpr n, Object arg) {
                    super.visit(n, arg);
                    VariableDeclarator vd = n.getVariable(0);
                    String name = vd.getNameAsString();
                    Type type = vd.getType();
                    identifiers.put(vd.getNameAsString(), new SimpleEntry<>(vd.getType(), null));

//                    System.out.println("VARIABLE DECLARATOR EXPRESSION");
//                    System.out.println("variables: "+ n.getVariables());
//                    System.out.println("modifiers: " + n.getModifiers());
                    //JavaParserSimpleFile.listNodes(n);
//                    System.out.println("VariableDeclarationExpr");
//                    System.out.println(" - " + n);
                }

                @Override
                public void visit(FieldDeclaration n, Object arg) {
                    super.visit(n, arg);


//                    System.out.println("VARIABLE DECLARATOR EXPRESSION");
                    System.out.println("expr: "+ n.toString());
                    System.out.println("variables: "+ n.getVariables());
                    VariableDeclarator vd = n.getVariable(0);
                    System.out.println("modifiers: " + n.getModifiers());
                    Modifier mod = null;
                    if (n.getModifiers().contains(FINAL)){
                        mod = FINAL;
                    }
                    identifiers.put( vd.getNameAsString(), new AbstractMap.SimpleEntry<>(vd.getType(), mod ));

                    //JavaParserSimpleFile.listNodes(n);
//                    System.out.println("VariableDeclarationExpr");
//                    System.out.println(" - " + n);
                }

                @Override
                public void visit(MethodDeclaration md, Object arg) {
                    super.visit(md, arg);
                    System.out.println("METHOD NAME " + md.getName());
                    System.out.println("METHOD TYPE " + md.getType());
                    methods.put(md.getNameAsString(), md.getType());
                }

                @Override
                public void visit(PrimitiveType n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println("PrimitiveType: " + n.resolve());
                }

                @Override
                public void visit(ClassOrInterfaceType n, Object arg) { // change object to identifierinfo
                    super.visit(n, arg);
//                    try {
//                        System.out.println("ClassOrInterfaceType: " + n.resolve().asReferenceType().getQualifiedName());
//                    } catch (UnsolvedSymbolException usex) {
//                        System.out.println("  -> Unrecognised:" + n.getNameAsString());
//                    }
                }
            };
            System.out.println("MODULES");
            // create a new identifierinfo object here, pass it to the visitor as the 2nd arg instead of null
            visitor.visit(compilationUnit, null);
            TypographyGuidelines typographyGuidelines = new TypographyGuidelines(identifiers, methods);
            numViolations += typographyGuidelines.checkUnderscores();
            System.out.println("num violations: " + numViolations);
//        }

    }
}
