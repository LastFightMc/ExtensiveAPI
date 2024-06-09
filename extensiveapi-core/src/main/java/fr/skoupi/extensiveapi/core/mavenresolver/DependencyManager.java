package fr.skoupi.extensiveapi.core.mavenresolver;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import fr.skoupi.extensiveapi.core.classloader.URLClassLoaderAccess;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class DependencyManager {

    @SuppressWarnings("Guava")
    public static final Supplier<URLClassLoaderAccess> URL_INJECTOR = Suppliers.memoize(() -> URLClassLoaderAccess.create((URLClassLoader) DependencyManager.class.getClassLoader()));

    private final DependencyDownloader dependencyDownloader;
    private final List<Dependency> toLoad;

    private final List<String> loaded;

    public DependencyManager() {
        this.toLoad = new ArrayList<>();
        this.loaded = new ArrayList<>();
        this.dependencyDownloader = new DependencyDownloader();
    }

    /**
     * This function allow to put the dependency in a list before being downloaded in parallel.
     *
     * @param dependency The object of the dependency we want to download
     */
    public void preLoad(Dependency dependency) {
        if (!this.toLoad.contains(dependency)) this.toLoad.add(dependency);
    }

    /**
     * This function start the process to download the dependencies in parallel.
     *
     * @param outputDir the folder where the dependency will be placed
     */
    public DependencyManager downloadJars(File outputDir) {
        this.dependencyDownloader.download(this.toLoad, outputDir, file -> {});
        return this;
    }

    public void injectJarsInsideFolder(File dependencyFolder) {
        try {
            if (!dependencyFolder.exists()) return;
            File[] files = dependencyFolder.listFiles(file -> file.getName().endsWith(".jar"));
            if (files == null || files.length == 0) return;
            for (File file : files) {
                injectSingleJar(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void injectSingleJar(File file) {
        try {
            if (!loaded.contains(file.getName())) {
                URL_INJECTOR.get().addURL(file.toURI().toURL());
                this.loaded.add(file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}