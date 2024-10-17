/*  DependencyDownloderTest
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 17/10/2024
 */

package fr.skoupi.extensiveapi.core.mavendownloader;

import fr.skoupi.extensiveapi.core.mavenresolver.Dependency;
import fr.skoupi.extensiveapi.core.mavenresolver.DependencyDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DependencyDownloaderTest {

    private DependencyDownloader downloader;
    private Dependency dependency;
    private File outputDir;
    private File dependencyFile;
    private Consumer<File> afterDownload;

    @BeforeEach
    void setUp() {
        downloader = new DependencyDownloader();
        dependency = new Dependency("org.redisson", "redisson", "3.37.0", "https://repo1.maven.org/maven2/");
        outputDir = new File("test-output");
        dependencyFile = new File(outputDir, "redisson-3.37.0.jar");
        afterDownload = mock(Consumer.class);
    }

    @Test
    @DisplayName("Test download redisson")
    void testDownloadDependency() {
        downloader.downloadDependency(dependency, outputDir, afterDownload);

        // Verify that the afterDownload consumer was called
        verify(afterDownload, times(1)).accept(dependencyFile);

        // Verify that the file exists and is readable
        assertTrue(dependencyFile.exists(), "The dependency file should exist.");
        assertTrue(dependencyFile.isFile(), "The dependency file should be a file.");
        assertTrue(dependencyFile.canRead(), "The dependency file should be readable.");

        // Log the results
        Logger.getLogger("ExtensiveAPI - DependencyDownloader").info("Downloaded " + dependencyFile.getName());
        Logger.getLogger("ExtensiveAPI - DependencyDownloader").info("File exists: " + dependencyFile.exists());
    }
}