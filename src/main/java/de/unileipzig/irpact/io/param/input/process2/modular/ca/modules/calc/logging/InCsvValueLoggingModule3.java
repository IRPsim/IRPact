package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.CsvValueLoggingModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING_CSV;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = CALCLOG_LABEL,
//                shape = CALCLOG_SHAPE,
//                color = CALCLOG_COLOR,
//                border = CALCLOG_BORDER,
//                tags = {CALCLOG_GRAPHNODE}
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.CALCLOG_NODE
        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.CALCLOG_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.CALCLOG_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.CALCLOG_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING_CSV)
public class InCsvValueLoggingModule3 implements InConsumerAgentCalculationLoggingModule2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
//        setShapeColorFillBorder(res, thisClass(), CALCLOG_SHAPE, CALCLOG_COLOR, CALCLOG_FILL, CALCLOG_BORDER);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBaseName() {
        return getName();
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean enabled = true;
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logReevaluatorCall = false;
    public void setLogReevaluatorCall(boolean logReevaluatorCall) {
        this.logReevaluatorCall = logReevaluatorCall;
    }
    @Override
    public boolean isLogReevaluatorCall() {
        return logReevaluatorCall;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean logDefaultCall = true;
    public void setLogDefaultCall(boolean logDefaultCall) {
        this.logDefaultCall = logDefaultCall;
    }
    @Override
    public boolean isLogDefaultCall() {
        return logDefaultCall;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean printHeader = true;
    public void setPrintHeader(boolean printHeader) {
        this.printHeader = printHeader;
    }
    @Override
    public boolean isPrintHeader() {
        return printHeader;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean storeXlsx = false;
    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }
    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InMinimalCsvValueLoggingModule input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] inputModule;
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(inputModule, "x");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.inputModule = new InConsumerAgentCalculationModule2[]{first};
    }

    public InCsvValueLoggingModule3() {
    }

    public InCsvValueLoggingModule3(String name) {
        setName(name);
    }

    @Override
    public InCsvValueLoggingModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCsvValueLoggingModule3 newCopy(CopyCache cache) {
        InCsvValueLoggingModule3 copy = new InCsvValueLoggingModule3();
        return Dev.throwException();
    }

    @Override
    public CsvValueLoggingModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        CsvValueLoggingModule2 module = new CsvValueLoggingModule2();
        module.setName(getName());
        module.setBaseName(getName());
        module.setEnabled(isEnabled());
        module.setStoreXlsx(isStoreXlsx());
        module.setLogReevaluatorCall(isLogReevaluatorCall());
        module.setLogDefaultCall(isLogDefaultCall());
        module.setPrintHeader(isPrintHeader());
        try {
            module.setDir(parser.getOptions().getCreatedDownloadDir());
        } catch (IOException e) {
            throw new ParsingException(e);
        }
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }

    @Override
    public int getAgentIndex() {
        return CsvValueLoggingModule2.AGENT_INDEX;
    }

    @Override
    public int getIdIndex() {
        return CsvValueLoggingModule2.ID_INDEX;
    }

    @Override
    public int getProductIndex() {
        return CsvValueLoggingModule2.PRODUCT_INDEX;
    }

    @Override
    public int getTimeIndex() {
        return CsvValueLoggingModule2.TIME_INDEX;
    }

    @Override
    public int getValueIndex() {
        return CsvValueLoggingModule2.VALUE_INDEX;
    }

    @Override
    public LocalDateTime toTime(String input) {
        return CsvValueLoggingModule2.toTime(input);
    }
}
