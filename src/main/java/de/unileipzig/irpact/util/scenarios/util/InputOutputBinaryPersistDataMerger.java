package de.unileipzig.irpact.util.scenarios.util;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.IRPFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class InputOutputBinaryPersistDataMerger implements ScenarioFileMerger {

    public static final InputOutputBinaryPersistDataMerger INSTANCE = new InputOutputBinaryPersistDataMerger();

    protected PrettyPrinter prettyPrinter;

    public InputOutputBinaryPersistDataMerger() {
        this(JsonUtil.DEFAULT);
    }

    public InputOutputBinaryPersistDataMerger(PrettyPrinter prettyPrinter) {
        this.prettyPrinter = prettyPrinter;
    }

    public void setPrettyPrinter(PrettyPrinter prettyPrinter) {
        this.prettyPrinter = prettyPrinter;
    }

    public PrettyPrinter getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public void merge(
            Path pathToInput, Charset firstCharset,
            Path pathToOutput, Charset secondCharset,
            Path pathToMerged, Charset outputCharset) throws IOException {
        ObjectNode input = JsonUtil.readJson(pathToInput, firstCharset);
        ObjectNode output = JsonUtil.readJson(pathToOutput, secondCharset);
        ObjectNode result = merge(input, output, false);
        JsonUtil.writeJson(result, pathToMerged, outputCharset, getPrettyPrinter());
    }

    public static ObjectNode merge(ObjectNode input, ObjectNode output, boolean useCopy) {
        ObjectNode merged = useCopy ? input.deepCopy() : input;

        IRPFile mergedFile = ContentTypeDetector.map(merged);
        IRPFile outputFile = ContentTypeDetector.map(output);

        if(mergedFile.numberOfEntries() != 1) {
            throw new IllegalArgumentException("supportes only files with one entry, input contains: " + mergedFile.numberOfEntries());
        }
        if(outputFile.numberOfEntries() != 1) {
            throw new IllegalArgumentException("supportes only files with one entry, output contains: " + outputFile.numberOfEntries());
        }

        JsonPointer ptrToInputSets = mergedFile.sets(mergedFile.buildDefaultIndices(0), "");
        JsonPointer ptrToOutputBinaryData = outputFile.sets(outputFile.buildDefaultIndices(0), InRoot.SET_BINARY_PERSIST_DATA);

        ObjectNode inputSets = (ObjectNode) mergedFile.root().at(ptrToInputSets);
        ObjectNode binaryData = (ObjectNode) outputFile.root().at(ptrToOutputBinaryData);

        inputSets.set(InRoot.SET_BINARY_PERSIST_DATA, binaryData);

        return merged;
    }
}
