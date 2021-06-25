package de.unileipzig.irpact.util.scenarios.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface ScenarioFileMerger {

    default void merge(Path first, Path second, Path output) throws IOException {
        merge(
                first, StandardCharsets.UTF_8,
                second, StandardCharsets.UTF_8,
                output, StandardCharsets.UTF_8
        );
    }

    void merge(
            Path first, Charset firstCharset,
            Path second, Charset secondCharset,
            Path output, Charset outputCharset) throws IOException;
}
