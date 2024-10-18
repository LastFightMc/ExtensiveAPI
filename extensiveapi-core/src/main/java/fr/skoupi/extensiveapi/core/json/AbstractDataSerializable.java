package fr.skoupi.extensiveapi.core.json;

/*  AbstractDataSerializable
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class AbstractDataSerializable<T> {

    private final Logger logger = Logger.getLogger("ExtensiveCore - JacksonAPI");
    protected boolean debugEnabled = false;


    /**
     * It loads a file and returns the object of the class that was passed in
     *
     * @param file   The file to load from
     * @param tClass The class of the object you want to load.
     * @return A MinecraftObjectMapper object
     */
    public T load(File file, Class<T> tClass, ObjectMapper objectMapper) {

        try {
            T type = objectMapper.readValue(file, tClass);

            if (debugEnabled)
                logger.info("File ".concat(file.getAbsolutePath()).concat(" has been loaded"));
            return type;
        } catch (IOException e) {
            if (debugEnabled)
                logger.warning("File ".concat(file.getAbsolutePath()).concat(" could not be loaded"));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * It saves an object to a file
     *
     * @param file   The file to save the object to.
     * @param object The object to save
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save(File file, Object object, ObjectMapper objectMapper) {
        if (file.getParentFile() != null)
            file.getParentFile().mkdirs();

        try {
            objectMapper.writeValue(file, object);
            if (debugEnabled)
                logger.info("File ".concat(file.getAbsolutePath()).concat(" has been saved"));
        } catch (IOException e) {
            if (debugEnabled)
                logger.warning("File ".concat(file.getAbsolutePath()).concat(" could not be saved"));
            e.printStackTrace();
        }
    }

}