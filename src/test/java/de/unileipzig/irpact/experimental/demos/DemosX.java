package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.res.BasicResourceLoader;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irpact.misc.ExampleFactory;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author Daniel Abitz
 */
@Disabled
public class DemosX {

    @Test
    void runStartWithTools() {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "--irptools",
                "--inputRootClass", InRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", Demo1.class.getName(),
                "--outDir", dir.toString(),
                "--charset", Util.windows1252().name(),
                "--validate",
                "--skipReference",
                "--dummyNomenklatur",
                "--pathToJava", TestFiles.java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", TestFiles.frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", TestFiles.backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString(),
                "--sortAfterPriority"
        };
        Start.main(args);
    }
    @Test
    void runToSpec() {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default.json").toString(),
                "--paramToSpec", dir.resolve("spec").toString()
        };
        Start.main(args);
    }
    @Test
    void runToParam() {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "--specToParam", dir.resolve("spec").toString(),
                "-o", dir.resolve("scenarios").resolve("default.spec.json").toString()
        };
        Start.main(args);
    }

    @Test
    void wtf() throws IOException {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String content0 = Util.readString(dir.resolve("scenarios").resolve("default.json"), StandardCharsets.UTF_8);
        String content1 = Util.readString(dir.resolve("scenarios").resolve("default.spec.json"), StandardCharsets.UTF_8);
        Assertions.assertEquals(content0, content1);
    }

    @Test
    void runMyStuff() throws Exception {
        Start start = Start.testMode();
        IRPact act = new IRPact(start, new BasicResourceLoader());
        AnnualEntry<InRoot> entry = ExampleFactory.buildMinimalExampleWith2Groups();
        act.start(entry);
    }

    @Test
    void MpaFun() {
        NavigableMap<Integer, String> map = new TreeMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
        map.put(4, "d");
        System.out.println(map.headMap(3, true));
    }

    @Test
    void MpaFun2() {
        NavigableMap<Timestamp, String> map = new TreeMap<>();
        ZonedDateTime now = ZonedDateTime.now();
        map.put(new BasicTimestamp(now), "a");
        map.put(new BasicTimestamp(now.plusDays(1)), "b");
        map.put(new BasicTimestamp(now.plusDays(2)), "c");
        map.put(new BasicTimestamp(now.plusDays(3)), "d");
        System.out.println(map.headMap(new BasicTimestamp(now.plusDays(2)), true));
        
        System.out.println(map);
    }
}
