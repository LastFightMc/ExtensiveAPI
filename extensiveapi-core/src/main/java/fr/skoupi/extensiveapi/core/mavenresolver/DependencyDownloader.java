package fr.skoupi.extensiveapi.core.mavenresolver;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DependencyDownloader {

    /**
     * Own sha1 calculator
     *
     * @param file we need to calculate
     * @return the Sha1 of the files
     * @throws IOException              if the file does not exist
     * @throws NoSuchAlgorithmException if the diggest does not contain this one
     */
    private static String calcSHA1(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), sha1)) {
            while (dis.read() != -1) ;
            sha1 = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : sha1.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }

    /**
     * This function check if the dependency is already downloaded or not and download it if needed
     *
     * @param dependency    the object Depency that contains group, artifact name and version of the jar
     * @param outputDir     the directory where the dependency will be downloaded
     * @param afterDownload the function that will be executed after the download
     */
    public void downloadDependency(Dependency dependency, File outputDir, Consumer<File> afterDownload) {
        String name = dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar";
        File dependencyFile = new File(outputDir + File.separator + name);
        boolean fileExists = dependencyFile.exists() && dependencyFile.isFile();

        if (fileExists) {
            if (checksumDependency(dependency, dependencyFile)) {
                afterDownload.accept(dependencyFile);
            }
            return;
        }

        download(dependency.getURLName(), dependencyFile);

        boolean fileExist2 = dependencyFile.exists() && dependencyFile.isFile();
        if (fileExist2) {
            if (checksumDependency(dependency, dependencyFile)) {
                afterDownload.accept(dependencyFile);
            }
        }

    }

    /**
     * This function allow us to download a file (here the jar)
     *
     * @param urlLink        the url of the file we wanna download
     * @param downloadFolder the directory where the dependency will be downloaded
     */

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void download(String urlLink, File downloadFolder) {
        try {
            URL url = new URL(urlLink);
            downloadFolder.mkdirs();
            Files.copy(url.openStream(), downloadFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(List<Dependency> dependencies, File outputDir, Consumer<File> afterDownload) {
        for (final Dependency dependency : dependencies) {
            downloadDependency(dependency, outputDir, afterDownload);
        }
    }

    /**
     * This function will check if the sha1 of the remote depency and the sha1 of our previous downloaded dependency are the same
     * if not, the file is corrupted and we need to delete it
     *
     * @param dependency    the object to check
     * @param dependecyFile the file where is located the dependency
     * @return true if the two sha1 are the same
     */

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean checksumDependency(Dependency dependency, File dependecyFile) {
        try {
            InputStream stream = new URL(dependency.getURLName() + ".sha1").openStream();
            final String urlSHa1 = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            final String dependencySha1 = calcSHA1(dependecyFile).toLowerCase();

            if (!urlSHa1.equals(dependencySha1)) {
                dependecyFile.delete();
                return false;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            dependecyFile.delete();
            return false;
        }
        return true;
    }

}