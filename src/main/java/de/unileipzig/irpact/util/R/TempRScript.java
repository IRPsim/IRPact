package de.unileipzig.irpact.util.R;

import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class TempRScript extends AbstractRScript {

    public static Supplier<? extends Path> DEFAULT_TEMP_FILE = () -> {
        try {
            return Files.createTempFile("TempRScript-", ".R");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    };

    protected final List<String> lines = new ArrayList<>();
    protected Supplier<? extends Path> tempFileSupplier;
    protected Path tempFile;
    protected Charset tempFileCharset;

    public TempRScript() {
        this(DEFAULT_TEMP_FILE);
    }

    public TempRScript(Supplier<? extends Path> tempFileSupplier) {
        this.tempFileSupplier = tempFileSupplier;
    }

    @Override
    protected String printRScriptPath() {
        return "\"" + tempFile + "\"";
    }

    public String printTempFile() throws IOException {
        return notExists() ? null : Util.readString(tempFile, getTempFileCharset0());
    }

    public void setTempFile(Path tempFile) {
        this.tempFile = tempFile;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }

    public Charset getTempFileCharset() {
        return tempFileCharset;
    }

    protected Charset getTempFileCharset0() {
        return tempFileCharset == null ? Charset.defaultCharset() : tempFileCharset;
    }

    public void create(OpenOption... openOptions) throws IOException {
        Charset cs = getTempFileCharset0();
        if(tempFile == null) {
            tempFile = tempFileSupplier.get();
        }
        Files.write(tempFile, lines, cs, openOptions);
    }

    public boolean exists() {
        return tempFile != null && Files.exists(tempFile);
    }

    public boolean notExists() {
        return tempFile == null || Files.notExists(tempFile);
    }

    public void cleanUp() throws IOException {
        if(tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Override
    public void execute(R engine) throws IOException, InterruptedException, RScriptException {
        boolean notExists = notExists();
        if(notExists) {
            create();
        }
        try {
            super.execute(engine);
        } finally {
            if(notExists) {
                cleanUp();
            }
        }

    }
}
