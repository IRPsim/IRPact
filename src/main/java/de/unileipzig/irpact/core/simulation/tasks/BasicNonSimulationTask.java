package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.ResourceLoader;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class BasicNonSimulationTask extends AbstractTask implements NonSimulationTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicNonSimulationTask.class);

    public static final String TASK = "t";
    public static final long ID = -200L;

    public static final int NO_TASK = -1;
    public static final int HELLO_WORLD = 1;
    public static final int RES_TEST = 2;

    public BasicNonSimulationTask() {
        super(IRPactJson.SMILE.createObjectNode());
        setInfo("no info");
        setTaskNumber(NO_TASK);
    }

    public BasicNonSimulationTask(byte[] data) throws IOException {
        super((ObjectNode) IRPactJson.fromBytesWithSmile(data));
    }

    public void setTaskNumber(int taskNumber) {
        node.put(TASK, taskNumber);
    }

    public int getTaskNumber() {
        JsonNode jn = node.get(TASK);
        if(jn == null || !jn.isNumber()) {
            return -1;
        } else {
            return jn.intValue();
        }
    }

    @Override
    protected long getID() {
        return ID;
    }

    @Override
    public void run() throws Exception {
        int taskNumber = getTaskNumber();
        switch (taskNumber) {
            case HELLO_WORLD:
                callHelloWorld();
                break;

            case RES_TEST:
                callResTest();
                break;

            default:
                LOGGER.debug("task '{}' has no task number", getInfo());
        }
    }

    //=========================
    //some lazy stuff
    //=========================

    private void callHelloWorld() {
        LOGGER.info("{}: HELLO WORLD", getInfo());
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    private void callResTest() {
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
            throw new RuntimeException("callResTest failed", e);
        }
    }
}
