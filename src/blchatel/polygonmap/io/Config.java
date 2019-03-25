package blchatel.polygonmap.io;

import org.ini4j.Profile;
import org.ini4j.Wini;
import java.io.IOException;

/**
 * Ini configuration class. From a file system and a filename, this tool class
 * allow to access parameters by section and option name
 * @see FileSystem
 */
public class Config {

    /// The ini file
    private final String filename;
    private final Wini ini;

    /**
     * Default Config constructor
     * @param fileSystem (FileSystem): the file system
     * @param filename (String): the filename (should be a ini file)
     */
    public Config(FileSystem fileSystem, String filename){

        this.filename = filename;

        try {
            if(!filename.toLowerCase().endsWith(".ini"))
                throw new IllegalArgumentException("Filename should be .ini format");
            ini = new Wini(fileSystem.read(filename));

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Filename should be .ini format");
        }
    }


    /**
     * Parameter getter by section and option name
     * @param sectionName (Object) the section name (must exist)
     * @param optionName (Object): the option name (must exist in section)
     * @return (String): the parameter value as a string
     */
    public String get(Object sectionName, Object optionName){
        String value = ini.get(sectionName, optionName);
        if(value == null){
            throw new IllegalArgumentException("Value for "+optionName+" in section "+sectionName+" does not exists");
        }
        return value;
    }

    /**
     * Parameter getter by section and option name
     * @param sectionName (Object) the section name (must exist)
     * @param optionName (Object): the option name (must exist in section)
     * @param optionName (Class of T): the class of the output (parse must be possible between option string value and T)
     * @return (T): the parameter value as a T object
     */
    public <T> T get(Object sectionName, Object optionName, Class<T> clazz) {
        T value = ini.get(sectionName, optionName, clazz);
        if(value == null){
            throw new IllegalArgumentException("Value for "+optionName+" in section "+sectionName+" does not exists");
        }
        return value;
    }


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder("Configuration read from file '" + filename + '\'' +
                "\nNumber of sections: " + ini.size() + "\n");

        for (String sectionName: ini.keySet()) {
            s.append("[").append(sectionName).append("]\n");
            Profile.Section section = ini.get(sectionName);
            for (String optionKey: section.keySet()) {
                s.append("\t").append(optionKey).append("=").append(section.get(optionKey)).append("\n");
            }
        }
        return s.toString();
    }
}
