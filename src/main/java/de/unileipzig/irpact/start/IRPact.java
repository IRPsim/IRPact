package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.ResourceLoader;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class IRPact {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IRPact.class);

    /**
     * Current version.
     * first digit: major version (large update)
     * second digit: minor version (small update)
     * third digit: patch version (fixes)
     *
     * Used to identify valid input data.
     */
    public static final String VERSION = "v0_0_0";

    private final Start clParam;
    private final ObjectNode inRoot;

    public IRPact(Start clParam, ObjectNode inRoot) {
        this.clParam = clParam;
        this.inRoot = inRoot;
    }

    public void start() {
        fixCheck();
        throw new RuntimeException("NOCH NICHT");
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    private void fixCheck() {
        try {
            URL url1 = ResourceLoader.load("temp/x.txt");
            if(url1 != null) {
                LOGGER.debug("x found: {}", url1);
                try(InputStream in = ResourceLoader.open("temp/x.txt");
                    Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader bReader = new BufferedReader(reader)) {
                    LOGGER.debug(bReader.readLine());
                    LOGGER.debug(bReader.readLine());
                }
            } else {
                LOGGER.debug("x not found");
            }
            URL url2 = ResourceLoader.load("temp/y.txt");
            if(url2 != null) {
                LOGGER.debug("y found: {}", url2);
                try(InputStream in = ResourceLoader.open("temp/y.txt");
                    Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader bReader = new BufferedReader(reader)) {
                    LOGGER.debug(bReader.readLine());
                    LOGGER.debug(bReader.readLine());
                }
            } else {
                LOGGER.debug("y not found");
            }
            URL url3 = ResourceLoader.load("temp/z.txt");
            if(url3 != null) {
                LOGGER.debug("z found: {}", url3);
                try(InputStream in = ResourceLoader.open("temp/z.txt");
                    Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader bReader = new BufferedReader(reader)) {
                    LOGGER.debug(bReader.readLine());
                    LOGGER.debug(bReader.readLine());
                }
            } else {
                LOGGER.debug("z not found");
            }
            //===
            LOGGER.debug("decode test");
            StringBuilder sb = new StringBuilder();
            sb.append("start: dir check").append('\n');
            Path currentDir = Paths.get(".");
            sb.append("dir: " + currentDir).append('\n');
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
                for(Path path: stream) {
                    sb.append("> " + path).append('\n');
                }
            }
            sb.append("ende: dir check").append('\n');
            String text = sb.toString();
            String b32act = IRPactBase32.encodeUTF8ToString(text);
            LOGGER.debug(b32act);
        } catch (Exception e) {
            throw new RuntimeException("fixCheck failed", e);
        }
    }
}
