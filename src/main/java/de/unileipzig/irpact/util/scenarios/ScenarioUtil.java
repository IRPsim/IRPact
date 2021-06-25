package de.unileipzig.irpact.util.scenarios;

import com.fasterxml.jackson.core.PrettyPrinter;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irptools.io.IRPFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class ScenarioUtil {

    protected ScenarioUtil() {
    }

    public static void store(IRPFile file, Path target) throws IOException {
        JsonUtil.writeJson(file.root(), target, StandardCharsets.UTF_8, JsonUtil.DEFAULT);
    }

    public static void storeMinimal(IRPFile file, Path target) throws IOException {
        JsonUtil.writeJson(file.root(), target, StandardCharsets.UTF_8, JsonUtil.MINIMAL);
    }

    public static void storePretty(IRPFile file, Path target) throws IOException {
        JsonUtil.writeJson(file.root(), target, StandardCharsets.UTF_8, JsonUtil.PRETTY);
    }

    public static void store(IRPFile file, Path target, Charset charset) throws IOException {
        JsonUtil.writeJson(file.root(), target, charset, JsonUtil.DEFAULT);
    }

    public static void store(IRPFile file, Path target, PrettyPrinter printer) throws IOException {
        JsonUtil.writeJson(file.root(), target, StandardCharsets.UTF_8, printer);
    }

    public static void store(IRPFile file, Path target, Charset charset, PrettyPrinter printer) throws IOException {
        JsonUtil.writeJson(file.root(), target, charset, printer);
    }
}
