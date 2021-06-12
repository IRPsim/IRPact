package de.unileipzig.irpact.commons.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class FileUtil {

    private FileUtil() {
    }

    public static boolean deleteDirIfExists(Path dir) throws IOException {
        if(Files.exists(dir)) {
            if(Files.isDirectory(dir)) {
                DeleteDirVisitor visitor = new DeleteDirVisitor();
                Files.walkFileTree(dir, visitor);
                return true;
            } else {
                throw new NotDirectoryException(dir.toString());
            }
        } else {
            return false;
        }
    }

    public static boolean deleteContentIfExists(Path dir) throws IOException {
        if(Files.exists(dir)) {
            if(Files.isDirectory(dir)) {
                DeleteDirVisitor visitor = new DeleteDirVisitor(dir);
                Files.walkFileTree(dir, visitor);
                return true;
            } else {
                throw new NotDirectoryException(dir.toString());
            }
        } else {
            return false;
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class DeleteDirVisitor implements FileVisitor<Path> {

        private final Path ignore;

        private DeleteDirVisitor() {
            this(null);
        }

        private DeleteDirVisitor(Path ignore) {
            this.ignore = ignore;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(file);
            Objects.requireNonNull(attrs);
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            Objects.requireNonNull(file);
            throw exc;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Objects.requireNonNull(dir);
            if(exc != null) {
                throw exc;
            } else {
                if(!Objects.equals(ignore, dir)) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        }
    }
}
