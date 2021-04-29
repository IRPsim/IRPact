package de.unileipzig.irpact.misc.logfile;

import de.unileipzig.irpact.misc.logfile.modify.LogFileLineModifier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface LogFileLines extends LogFile {

    List<String> toList();

    int totalLines();

    String getLine(int index);

    void modifyThis(LogFileLineModifier modifier);

    void addLine(int index, String line);

    default void writeTo(Path path, Charset charset, OpenOption... options) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, charset, options)) {
            writeTo(writer);
        }
    }

    default void writeTo(BufferedWriter target) throws IOException {
        for(int i = 0; i < totalLines(); i++) {
            if(i > 0) {
                target.newLine();
            }
            target.write(getLine(i));
        }
    }

    void sortThis(Comparator<? super String> c);
}
