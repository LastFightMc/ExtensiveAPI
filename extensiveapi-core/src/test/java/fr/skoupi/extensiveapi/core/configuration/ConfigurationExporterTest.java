package fr.skoupi.extensiveapi.core.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationExporterTest {


   /* @DisplayName("Export Config ")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @ParametersAreNonnullByDefault
    void exportConfigTest(boolean replace) {

        File file = new File("test.txt");

        InputStream inputStream = getClass().getResourceAsStream("/test.txt");

        if (inputStream == null) Assertions.fail("InputStream is null");

        InputStream finalInputStream = inputStream;
        Assertions.assertDoesNotThrow(() -> ConfigurationExporter.createConfig(file, finalInputStream, false));

        if (replace) {
            inputStream = getClass().getResourceAsStream("/test2.txt");
            if (inputStream == null) Assertions.fail("Test 2 InputStream is null");
            InputStream finalInputStream1 = inputStream;
            Assertions.assertDoesNotThrow(() -> ConfigurationExporter.createConfig(file, finalInputStream1, true));
        }

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.canRead());

        byte[] exported = Assertions.assertDoesNotThrow(() -> Files.readAllBytes(file.toPath()));
        Assertions.assertNotNull(exported);
        Assertions.assertTrue(exported.length > 0);

        Path path = Path.of(Assertions.assertDoesNotThrow(() -> getClass().getResource("/test.txt")).getPath());
        if (replace)
            path = Path.of(Assertions.assertDoesNotThrow(() -> getClass().getResource("/test2.txt")).getPath());

        Path finalPath = path;
        Assertions.assertDoesNotThrow(() -> Files.readAllBytes(finalPath));

    }

    */

}
