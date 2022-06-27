import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Utility {

    /**
     * Find all the Java source files reachable from the paths given as the specified roots.
     * If any of the roots specify directories, then recursively search those directories.
     * If any of the roots specify an existing Java source file, then include it.
     * If any of the roots do not exist (whether file or directory) then an exception is thrown
     * If the paths are relative to the JVM.
     * TODO what happens to an empty string?
     * @param roots A list of paths to start the search from.
     * @return All Java source files reachable from the specified roots.
     * @throws java.nio.file.NoSuchFileException if the specified roots do not exist
     * @throws IOException If there is any other IO problem.
     */
    public static Set<Path> findAllJavaSourceFilesFromRoots(String... roots) throws NoSuchFileException, IOException {
        Set<Path> result = new HashSet<Path>();
        for (String root: roots) {
            result = addAllJavaSourceFilesFromRoot(result, root);
        }
        return result;
    }

    /**
     * This is currently public in the belief someone might want to call this directly
     * @param existing
     * @param pathToRoot
     * @return
     * @throws java.nio.file.NoSuchFileException
     * @throws IOException
     */
    public static Set<Path> addAllJavaSourceFilesFromRoot(Set<Path> existing, String pathToRoot)
            throws java.nio.file.NoSuchFileException, IOException {
        Set<Path> result = new HashSet<Path>(existing);
        Files.walk(Paths.get(pathToRoot))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".java") )
                .forEach(path -> { result.add(path); } );
        return result;
    }
}