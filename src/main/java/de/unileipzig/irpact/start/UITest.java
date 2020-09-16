package de.unileipzig.irpact.start;

import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;
import de.unileipzig.irpact.uitest.out.OutputRoot;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.input.InputData;
import de.unileipzig.irptools.io.input.InputFile;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class UITest {

    public static void main(String[] args) throws IOException {
        HardCodedAgentDemo demo = new HardCodedAgentDemo();
        CommandLine cmdLine = new CommandLine(demo);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            String outFileStr = demo.getOutputFile();
            Path outPath = Paths.get(outFileStr);

            OutputRoot root = OutputRoot.x();

            DefinitionCollection dcoll = AnnotationParser.parse(OutputRoot.CLASSES);
            DefinitionMapper dmap = new DefinitionMapper(dcoll);
            Converter converter = new Converter(dmap);

            InputData<OutputRoot> outData = new InputData<>(root);
            InputFile outFile = outData.serialize(converter);
            outFile.store(outPath);
        } else {
            System.exit(exitCode);
        }
    }
}
