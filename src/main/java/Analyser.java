import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class Analyser {
    /**
     * Maps paths to the ast for the source file the paths identify.
     * Determined lazily.
     */
    private Map<Path,CompilationUnit> _asts;

    /**
     * What TypeSolver to use. As a general rule, there will be multiple TypeSolvers, so we will
     * need a CombinedTypeSolver. It is initialised with ReflectionTypeSolver which will recognise any types
     * on the classpath (or so the documentation suggests - there's some indication third-party libraries
     * are not picked up, so @see {@link #addJar(String)}).
     */

    private CombinedTypeSolver _typeSolver;

    public Analyser() {
        _asts = new HashMap<Path, CompilationUnit>();
        _typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
    }

    public void addSourcePath(String sourcePath) {
        _typeSolver.add(new JavaParserTypeSolver(new File(sourcePath)));
    }

    public void addJar(String jarPath) throws IOException {
        _typeSolver.add(new JarTypeSolver(new File(jarPath)));
    }

    public CompilationUnit getCompilationUnitForPath(Path path) throws IOException {
        CompilationUnit result = _asts.get(path);
        if (result == null) {
            parsePath(path);
            result = _asts.get(path);
        }
        return result;
    }

    public Set<Path> getPathsWithCompilationUnits() {
        return _asts.keySet();
    }

    private void parsePath(Path path) throws IOException {
        ParserConfiguration configuration = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(_typeSolver));
        JavaParser parser = new JavaParser(configuration);
        ParseResult<CompilationUnit> parseResult = parser.parse(path);
        CompilationUnit compilationUnit = parseResult.getResult().get();
        _asts.put(path,  compilationUnit);
    }
}