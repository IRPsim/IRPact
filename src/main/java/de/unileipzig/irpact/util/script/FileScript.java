package de.unileipzig.irpact.util.script;

import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public abstract class FileScript<E> implements Script<E> {

    protected String text;
    protected Path path;
    protected Charset charset = StandardCharsets.UTF_8;
    protected boolean temp;

    protected boolean changed = true;

    public FileScript() {
    }

    public FileScript(String text) {
        setText(text);
    }

    public FileScript(Path path, Charset charset) {
        setPath(path);
        setCharset(charset);
    }

    public void setText(String text) {
        this.text = text;
        changed = true;
    }

    public String getText() {
        return text;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String printPath() {
        if(path == null) {
            throw new NoSuchElementException();
        }
        return path.toString();
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public boolean isTemp() {
        return temp;
    }

    public void load() throws IOException {
        load(getCharset());
    }

    public void load(Charset charset) throws IOException {
        load(getPath(), charset);
    }

    public void load(Path path, Charset charset) throws IOException {
        String text = Util.readString(path, charset);
        setPath(path);
        setCharset(charset);
        setText(text);
        changed = false;
    }

    public void store() throws IOException {
        store(getPath());
    }

    public void store(Path path) throws IOException {
        store(path, getCharset());
    }

    public void store(Path path, Charset charset) throws IOException {
        Util.writeString(getText(), path, charset);
    }

    public boolean exists() {
        return Files.exists(getPath());
    }

    public boolean notExists() {
        return Files.notExists(getPath());
    }

    public boolean delete() throws IOException {
        return Files.deleteIfExists(getPath());
    }

    public boolean deleteIfTemp() throws IOException {
        if(isTemp()) {
            return delete();
        } else {
            return false;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void update() throws IOException {
        if(isChanged()) {
            store();
            changed = false;
        }
    }

    protected abstract void execute0(E engine) throws IOException, InterruptedException, ScriptException;

    @Override
    public void execute(E engine) throws IOException, InterruptedException, ScriptException {
        boolean notExists = notExists();
        if(notExists) {
            store();
        }
        if(isChanged()) {
            update();
        }
        try {
            execute0(engine);
        } finally {
            if(notExists) {
                delete();
            }
        }
    }

    @Override
    public String print() {
        return getText();
    }
}
