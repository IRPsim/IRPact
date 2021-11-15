package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.CsvValueLoggingModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.CALCLOG_COLOR;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = CALCLOG_LABEL,
                shape = CALCLOG_SHAPE,
                color = CALCLOG_COLOR,
                border = CALCLOG_BORDER,
                tags = {CALCLOG_GRAPHNODE}
        )
)
public class InCsvValueLoggingModule_calcloggraphnode2 implements InConsumerAgentCalculationLoggingModule2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_CALC_CSV);
        setShapeColorFillBorder(res, thisClass(), CALCLOG_SHAPE, CALCLOG_COLOR, CALCLOG_FILL, CALCLOG_BORDER);

        addEntryWithDefaultAndDomain(res, thisClass(), "logReevaluatorCall", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "logDefaultCall", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "printHeader", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeXlsx", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "input_graphedge2");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;
    @Override
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getBaseName() {
        return getName();
    }

    @FieldDefinition
    public boolean logReevaluatorCall = false;
    public void setLogReevaluatorCall(boolean logReevaluatorCall) {
        this.logReevaluatorCall = logReevaluatorCall;
    }
    @Override
    public boolean isLogReevaluatorCall() {
        return logReevaluatorCall;
    }

    @FieldDefinition
    public boolean logDefaultCall = true;
    public void setLogDefaultCall(boolean logDefaultCall) {
        this.logDefaultCall = logDefaultCall;
    }
    @Override
    public boolean isLogDefaultCall() {
        return logDefaultCall;
    }

    @FieldDefinition
    public boolean printHeader = true;
    public void setPrintHeader(boolean printHeader) {
        this.printHeader = printHeader;
    }
    @Override
    public boolean isPrintHeader() {
        return printHeader;
    }

    @FieldDefinition
    public boolean storeXlsx = false;
    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }
    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InCsvValueLoggingModule input"}
            )
    )
    public InConsumerAgentCalculationModule2[] input_graphedge2;
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "x");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.input_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    public InCsvValueLoggingModule_calcloggraphnode2() {
    }

    @Override
    public InCsvValueLoggingModule_calcloggraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCsvValueLoggingModule_calcloggraphnode2 newCopy(CopyCache cache) {
        InCsvValueLoggingModule_calcloggraphnode2 copy = new InCsvValueLoggingModule_calcloggraphnode2();
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
    public int getTimeIndex() {
        return CsvValueLoggingModule2.TIME_INDEX;
    }

    @Override
    public int getProductIndex() {
        return CsvValueLoggingModule2.PRODUCT_INDEX;
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
