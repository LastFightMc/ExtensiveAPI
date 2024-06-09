package fr.skoupi.extensiveapi.core.configuration;

/*  ConfigurationExporter
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class ConfigurationExporter {

    /**
     * If the target file exists, replace it with the input stream. If it doesn't exist, create it
     *
     * @param targetFile The file to create.
     * @param in         The input stream of the file you want to copy.
     * @param replace    If the file already exists, should it be replaced?
     * @return The file that was created.
     */
    public static File createConfig(File targetFile, InputStream in, boolean replace) throws IOException {
        if (targetFile.exists()) {
            if (replace) Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return targetFile;
        }
        Files.copy(in, targetFile.toPath());
        return targetFile;
    }

    /**
     * If the file doesn't exist, create it
     *
     * @param file The file to create.
     * @return A file object.
     */
    public static File createFile(final File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    /**
     * If the folder doesn't exist, create it
     *
     * @param folder The folder to create.
     * @return A file object.
     */
    public static File createFolders(final File folder) {
        if (!folder.exists()) {
            folder.getParentFile().mkdirs();
        }
        return folder;
    }

    /**
     * If the folder doesn't exist, create it.
     *
     * @param folder The folder to create.
     * @return A File object
     */
    public static File createFolder(final File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
}