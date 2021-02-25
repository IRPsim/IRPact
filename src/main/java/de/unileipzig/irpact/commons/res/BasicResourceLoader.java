package de.unileipzig.irpact.commons.res;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class BasicResourceLoader implements ResourceLoader {

    protected String resDir = "irpacttempdata";
    protected Path dir = Paths.get("irpactdata");

    public BasicResourceLoader() {
    }

    public void setResDir(String resDir) {
        this.resDir = resDir;
    }

    public String getResDir() {
        return resDir;
    }

    public void setDir(Path dir) {
        this.dir = dir;
    }

    public Path getDir() {
        return dir;
    }

    @Override
    public boolean exists(String fileName) {
        return hasPath(fileName) || hasResource(fileName);
    }

    //=========================
    //via dir
    //=========================

    protected Path resolve(String other) {
        return dir.resolve(other);
    }

    @Override
    public boolean hasPath(String fileName) {
        Path path = resolve(fileName);
        return Files.exists(path);
    }

    @Override
    public Path get(String fileName) {
        Path path = resolve(fileName);
        return Files.exists(path)
                ? path
                : null;
    }

    //=========================
    //via ClassLoader
    //=========================

    protected String getResourcePath(String fileName) {
        return resDir + "/" + fileName;
    }

    @Override
    public boolean hasResource(String fileName) {
        return getResource(fileName) != null;
    }

    @Override
    public URL getResource(String fileName) {
        String path = getResourcePath(fileName);
        return getResource0(path);
    }

    public static URL getResource0(String path) {
        return BasicResourceLoader.class.getClassLoader().getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String fileName) {
        String path = getResourcePath(fileName);
        return getResourceAsStream0(path);
    }

    public static InputStream getResourceAsStream0(String path) {
        return BasicResourceLoader.class.getClassLoader().getResourceAsStream(path);
    }
}
