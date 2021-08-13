package de.unileipzig.irpact.commons.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class FileUtil {

    private FileUtil() {
    }

    public static Path changeFileName(Path path, String prefix, String suffix) {
        if(prefix == null) prefix = "";
        if(suffix == null) suffix = "";
        String fileName = getFileName(path);
        String extension = getExtension(path);
        return extension == null
                ? path.resolveSibling(prefix + fileName + suffix)
                : path.resolveSibling(prefix + fileName + suffix + "." + extension);
    }

    public static String getExtension(Path path) {
        if(path == null) {
            throw new NullPointerException("path is null");
        }

        String fileNameStr = path.getFileName().toString();
        int dotIndex = fileNameStr.lastIndexOf('.');
        return dotIndex == -1
                ? null
                : fileNameStr.substring(dotIndex + 1);
    }

    public static String getFileName(Path path) {
        if(path == null) {
            throw new NullPointerException("path is null");
        }

        String fileNameStr = path.getFileName().toString();
        int dotIndex = fileNameStr.lastIndexOf('.');
        return dotIndex == -1
                ? fileNameStr
                : fileNameStr.substring(0, dotIndex);
    }

    public static Path createTempFile(Path dir, String prefix, String suffix) throws IOException {
        return dir == null
                ? Files.createTempFile(prefix, suffix)
                : Files.createTempFile(dir, prefix, suffix);
    }

    public static boolean deleteIfExists(Path... paths) throws IOException {
        boolean changed = false;

        List<IOException> failes = new ArrayList<>();
        for(Path path: paths) {
            if(path == null) {
                continue;
            }

            try {
                changed |= Files.deleteIfExists(path);
            } catch (IOException e) {
                failes.add(e);
            }
        }

        if(failes.size() > 0) {
            IOException master = new IOException("failed to delete " + failes.size() + " files");
            for(IOException e: failes) {
                master.addSuppressed(e);
            }
            throw master;
        }

        return changed;
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 8];
        int len;

        do {
            len = in.read(buf, 0, buf.length);
            if(len > 0) {
                baos.write(buf, 0, len);
            }
        } while(len != -1);

        return baos.toByteArray();
    }

    public static String readString(Path input, Charset charset) throws IOException {
        long size = Files.size(input);
        if(size > Integer.MAX_VALUE - 8) {
            throw new IllegalArgumentException("file too big: " + size);
        }
        StringBuilder sb = new StringBuilder((int) size);
        char[] cbuf = new char[8192];
        int len;
        try(Reader reader = Files.newBufferedReader(input, charset)) {
            while((len = reader.read(cbuf)) >= 0) {
                sb.append(cbuf, 0, len);
            }
        }
        return sb.toString();
    }

    public static String tryReadString(Path input, Charset charset) {
        try {
            return readString(input, charset);
        } catch (IOException e) {
            return null;
        }
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
