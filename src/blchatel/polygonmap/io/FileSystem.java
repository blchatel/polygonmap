package blchatel.polygonmap.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

/**
 * Represents a simple file system, where each file is associated to a unique name.
 */
public class FileSystem {

    private ClassLoader loader;

    /**
     * Creates a new resource file system using core binaires.
     */
    public FileSystem() {
        this.loader = FileSystem.class.getClassLoader();
    }

    /**
     * Open an existing file for read.
     * @param name (String): unique identifier, not null
     * @return (InputStream): content stream, not null
     * @throws IOException if file cannot be open for read
     */
    public InputStream read(String name) throws IOException {
        InputStream input = loader.getResourceAsStream(name);
        if (input != null) {
            return input;
        }
        throw new NoSuchFileException(name);
    }
}
