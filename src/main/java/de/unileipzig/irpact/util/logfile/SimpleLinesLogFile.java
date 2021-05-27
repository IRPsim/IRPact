package de.unileipzig.irpact.util.logfile;

import de.unileipzig.irpact.util.logfile.filters.LogFileLineFilter;
import de.unileipzig.irpact.util.logfile.modify.LogFileLineModifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SimpleLinesLogFile implements LinesLogFile {

    protected List<String> lines;

    public SimpleLinesLogFile() {
        this(new ArrayList<>());
    }

    public SimpleLinesLogFile(List<String> lines) {
        this.lines = lines;
    }

    public static SimpleLinesLogFile parse(Path path, Charset charset, LogFileLineFilter filter) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
            return parse(reader, filter);
        }
    }

    public static SimpleLinesLogFile parse(BufferedReader reader, LogFileLineFilter filter) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null) {
            if(filter != null && filter.accept(line)) {
                lines.add(line);
            }
        }
        return new SimpleLinesLogFile(lines);
    }

    @Override
    public List<String> toList() {
        return lines;
    }

    @Override
    public int totalLines() {
        return lines.size();
    }

    @Override
    public String getLine(int index) {
        return lines.get(index);
    }

    @Override
    public void modifyThis(LogFileLineModifier modifier) {
        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String modified = modifier.modify(line);
            lines.set(i, modified);
        }
    }

    @Override
    public void addLine(int index, String line) {
        lines.add(index, line);
    }

    @Override
    public void sortThis(Comparator<? super String> c) {
        lines.sort(c);
    }
}