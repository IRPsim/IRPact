package de.unileipzig.irpact.commons.resource;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicResourceLoader implements ResourceLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicResourceLoader.class);

    private static final Rnd RND = new Rnd();

    static {
        RND.enableSync();
    }

    protected String internalPath = INTERN_RESOURCES;
    protected Path externalPath = Paths.get(EXTERN_RESOURCES);

    public BasicResourceLoader() {
    }

    public void setInternalPath(String internalPath) {
        this.internalPath = internalPath;
    }

    public String getInternalPath() {
        return internalPath;
    }

    public void setExternalPath(Path externalPath) {
        this.externalPath = externalPath;
    }

    public Path getExternalPath() {
        return externalPath;
    }

    @Override
    public Path getTempPath(String prefix, String suffix) {
        return getTempPath(externalPath, prefix, suffix);
    }

    @Override
    public Path createTempPath(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(externalPath, prefix, suffix, attrs);
    }

    public static Path getTempPath(Path dir, String prefix, String suffix) {
        Path tempPath;
        if(prefix == null) prefix = "";
        if(suffix == null) suffix = "";
        do {
            long n = RND.nextLong();
            String name = prefix + Long.toUnsignedString(n, 16) + suffix;
            tempPath = dir.resolve(name);
        } while (Files.exists(tempPath));
        return tempPath;
    }

    @Override
    public boolean exists(String fileName) {
        return hasExternal(fileName) || hasInternal(fileName);
    }

    //=========================
    //via dir
    //=========================

    protected Path resolve(String other) {
        return externalPath.resolve(other);
    }

    @Override
    public boolean hasExternal(String fileName) {
        Path path = resolve(fileName);
        boolean has = Files.exists(path);
        LOGGER.trace(IRPSection.DEBUG, "has path '{}': {}", path, has);
        return has;
    }

    @Override
    public Path getExternal(String fileName) {
        Path path = resolve(fileName);
        return Files.exists(path)
                ? path
                : null;
    }

    @Override
    public InputStream getExternalAsStream(String fileName) {
        try {
            Path path = getExternal(fileName);
            return path != null && Files.exists(path)
                    ? Files.newInputStream(path)
                    : null;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //=========================
    //via ClassLoader
    //=========================

    protected String getResourcePath(String fileName) {
        return "/" + internalPath + "/" + fileName;
    }

    @Override
    public boolean hasInternal(String fileName) {
        boolean has = getInternal(fileName) != null;
        LOGGER.trace(IRPSection.DEBUG, "has resource '{}': {}", fileName, has);
        return has;
    }

    @Override
    public URL getInternal(String fileName) {
        String path = getResourcePath(fileName);
        return getResource0(path);
    }

    public static URL getResource0(String path) {
        return BasicResourceLoader.class.getResource(path);
    }

    @Override
    public InputStream getInternalAsStream(String fileName) {
        String path = getResourcePath(fileName);
        return getResourceAsStream0(path);
    }

    public static InputStream getResourceAsStream0(String path) {
        return BasicResourceLoader.class.getResourceAsStream(path);
    }
}
