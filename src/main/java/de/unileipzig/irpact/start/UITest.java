package de.unileipzig.irpact.start;

import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;
import picocli.CommandLine;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class UITest {

    public static void main(String[] args) throws IOException {
        HardCodedAgentDemo demo = new HardCodedAgentDemo();
        CommandLine cmdLine = new CommandLine(demo);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            demo.run();
        } else {
            System.exit(exitCode);
        }
    }
}
