package de.unileipzig.irpact.util.gis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

    private static void rename(ShapeFile from, ShapeFile to) throws IOException {
        renameIfExists(from.dbf(), to.dbf());
        renameIfExists(from.fix(), to.fix());
        renameIfExists(from.prj(), to.prj());
        renameIfExists(from.shp(), to.shp());
        renameIfExists(from.shx(), to.shx());
    }

    private static void renameIfExists(Path from, Path to) throws IOException {
        if(Files.exists(from)) {
            Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public ShapeFile rename(String newName) throws IOException {
        ShapeFile renamed = new ShapeFile(dir, newName);
        rename(this, renamed);
        return renamed;
    }

    public void renameThis(String newName) throws IOException {
        ShapeFile renamed = rename(newName);
        name = renamed.getName();
    }

    public boolean exists() {
        return Files.exists(dbf()) || Files.exists(shp());
    }

    public Path dbf() {
        return dir.resolve(name + ".dbf");
    }

    public Path fix() {
        return dir.resolve(name + ".fix");
    }

    public Path prj() {
        return dir.resolve(name + ".prj");
    }

    public Path shp() {
        return dir.resolve(name + ".shp");
    }

    public Path shx() {
        return dir.resolve(name + ".shx");
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

    private static long getSize(Path path) throws IOException {
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
