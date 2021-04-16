package de.unileipzig.irpact.util.gis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class ShapeFile {

    private Path dir;
    private String name;

    public ShapeFile(Path dir, String name) {
        this.dir = dir;
        this.name = name;
    }

    public Path getDir() {
        return dir;
    }

    public String getName() {
        return name;
    }

    public Path shp() {
        return dir.resolve(name + ".shp");
    }

    public Path dbf() {
        return dir.resolve(name + ".dbf");
    }

    public Path template() {
        return dir.resolve(name);
    }

    public long maxSize() throws IOException {
        return Math.max(
                getSize(shp()),
                getSize(dbf())
        );
    }

    private long getSize(Path path) throws IOException {
        if(Files.exists(path)) {
            return Files.size(path);
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return template().toString();
    }
}
