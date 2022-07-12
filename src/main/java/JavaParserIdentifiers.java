import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.VariableDeclaratorMetaModel;
import com.github.javaparser.resolution.UnsolvedSymbolException;

public class JavaParserIdentifiers {
    public static void main(String[] args) throws Exception {
        int numViolations = 0;
        String rootPath = "kalah_designs";
        String src1 = rootPath + "/design1041/src/kalah/TestingParser";
        Analyser analyser = new Analyser();
        analyser.addSourcePath(src1);
        Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(rootPath);
        Map<String, Type> identifiers = new HashMap<String, Type>();

        // Current hacky move: singling out Kalah class from design 1041 to check its variables
        // Prints all identifiers declared inside Kalah.java as of right now
        // Currently commenting out all the visit functionality we don't need: just printing identifier type and name.
        Path[] pathsList = paths.toArray(new Path[paths.size()]);
        Path path = pathsList[23];
//        for (Path path: paths) {
            CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
            VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(VariableDeclarator n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println("VariableDeclarator");
                    System.out.println(n.getType() + ": " + n.getName().toString());
                    identifiers.put( n.getName().toString(), n.getType());
//                    System.out.println(n.getParentNode());
                    VariableDeclaratorMetaModel x = n.getMetaModel();
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
                    //JavaParserSimpleFile.listNodes(n);
//                    System.out.println("VariableDeclarationExpr");
//                    System.out.println(" - " + n);
                }

                @Override
                public void visit(PrimitiveType n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println("PrimitiveType: " + n.resolve());
                }

                @Override
                public void visit(ClassOrInterfaceType n, Object arg) {
                    super.visit(n, arg);
//                    try {
//                        System.out.println("ClassOrInterfaceType: " + n.resolve().asReferenceType().getQualifiedName());
//                    } catch (UnsolvedSymbolException usex) {
//                        System.out.println("  -> Unrecognised:" + n.getNameAsString());
//                    }
                }
            };
            System.out.println("MODULES");
            visitor.visit(compilationUnit, null);
            TypographyGuidelines typographyGuidelines = new TypographyGuidelines(identifiers);
            numViolations += typographyGuidelines.checkUnderscores();
            System.out.println("num violations: " + numViolations);
//        }

    }
}
