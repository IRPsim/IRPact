package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.commons.util.ExceptionUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class ShapeFiles {

    private List<ShapeFile> files;

    public ShapeFiles() {
        this(new ArrayList<>());
    }

    public ShapeFiles(List<ShapeFile> files) {
        this.files = files;
    }

    public void add(ShapeFile shapeFile) {
        files.add(shapeFile);
    }

    public void addAll(Path dir) throws IOException {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path path: stream) {
                String fileName = path.getFileName().toString();
                if(fileName.endsWith(".shp")) {
                    String nameWithoutExtension = Gis.removeExtension(fileName);
                    ShapeFile shapeFile = new ShapeFile(dir, nameWithoutExtension);
                    add(shapeFile);
                }
            }
        }
    }

    public void addAll(Collection<? extends Path> dirs) throws IOException {
        for(Path dir: dirs) {
            addAll(dir);
        }
    }

    public int count() {
        return files.size();
    }

    public boolean isEmpty() {
        return files.isEmpty();
    }

    public List<ShapeFile> getFiles() {
        return files;
    }

    public List<Path> getShpFiles() {
        return files.stream()
                .map(ShapeFile::shp)
                .collect(Collectors.toList());
    }

    public List<Path> getDbfFiles() {
        return files.stream()
                .map(ShapeFile::dbf)
                .collect(Collectors.toList());
    }

    public long maxSize() throws IOException {
        long total = 0;
        for(ShapeFile shapeFile: getFiles()) {
            total += shapeFile.maxSize();
        }
        return total;
    }

    public ShapeFiles sortAfterMaxSize() {
        List<ShapeFile> sorted = files.stream()
                .sorted((o1, o2) -> {
                    try {
                        return Long.compare(o1.maxSize(), o2.maxSize());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());
        return new ShapeFiles(sorted);
    }

    public List<ShapeFiles> partAfterSize(long maxSize) throws IOException {
        List<ShapeFiles> coll = new ArrayList<>();
        ShapeFiles currentFiles = new ShapeFiles();
        long currentSize = 0L;

        for(ShapeFile next: getFiles()) {
            long nextSize = next.maxSize();
            if(nextSize >= maxSize) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "maxSize too small ({} < {})", maxSize, nextSize);
            }

            if(currentSize + nextSize >= maxSize) {
                coll.add(currentFiles);
                currentSize = 0;
                currentFiles = new ShapeFiles();
                System.out.println("!");
            }

            currentFiles.add(next);
            currentSize += nextSize;
        }

        if(!currentFiles.isEmpty()) {
            coll.add(currentFiles);
        }

        return coll;
    }

    public List<ShapeFiles> partAfterCount(int blockSize) {
        List<ShapeFiles> coll = new ArrayList<>();
        for(int i = 0; i < count(); i+= blockSize) {
            List<ShapeFile> block = files.stream()
                    .skip(i)
                    .limit(blockSize)
                    .collect(Collectors.toList());
            coll.add(new ShapeFiles(block));
        }
        return coll;
    }

    public ShapeFile getFirst() {
        return get(0);
    }

    public ShapeFile getLast() {
        return get(count() - 1);
    }

    public ShapeFile get(int index) {
        return files.get(index);
    }
}
