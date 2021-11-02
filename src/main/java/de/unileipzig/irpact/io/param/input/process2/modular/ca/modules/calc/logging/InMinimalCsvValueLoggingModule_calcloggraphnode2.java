package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.MinimalCsvValueLoggingModule2;
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
import java.time.ZonedDateTime;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

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
public class InMinimalCsvValueLoggingModule_calcloggraphnode2 implements InConsumerAgentCalculationLoggingModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_CALC_MINICSV);
        setShapeColorFillBorder(res, thisClass(), CALCLOG_SHAPE, CALCLOG_COLOR, CALCLOG_FILL, CALCLOG_BORDER);

        addEntryWithDefaultAndDomain(res, thisClass(), "skipReevaluatorCall", VALUE_TRUE, DOMAIN_BOOLEAN);
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
    public boolean skipReevaluatorCall = true;
    public void setSkipReevaluatorCall(boolean skipReevaluatorCall) {
        this.skipReevaluatorCall = skipReevaluatorCall;
    }
    @Override
    public boolean isSkipReevaluatorCall() {
        return skipReevaluatorCall;
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
                    tags = {"InMinimalCsvValueLoggingModule input"}
            )
    )
    public InConsumerAgentCalculationModule2[] input_graphedge2;
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "x");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.input_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    public InMinimalCsvValueLoggingModule_calcloggraphnode2() {
    }

    @Override
    public InMinimalCsvValueLoggingModule_calcloggraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMinimalCsvValueLoggingModule_calcloggraphnode2 newCopy(CopyCache cache) {
        InMinimalCsvValueLoggingModule_calcloggraphnode2 copy = new InMinimalCsvValueLoggingModule_calcloggraphnode2();
        return Dev.throwException();
    }

    @Override
    public MinimalCsvValueLoggingModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        MinimalCsvValueLoggingModule2 module = new MinimalCsvValueLoggingModule2();
        module.setName(getName());
        module.setBaseName(getName());
        module.setStoreXlsx(isStoreXlsx());
        module.setSkipReevaluatorCall(isSkipReevaluatorCall());
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
        return MinimalCsvValueLoggingModule2.AGENT_INDEX;
    }

    @Override
    public int getProductIndex() {
        return MinimalCsvValueLoggingModule2.PRODUCT_INDEX;
    }

    @Override
    public int getTimeIndex() {
        return MinimalCsvValueLoggingModule2.TIME_INDEX;
    }

    @Override
    public int getValueIndex() {
        return MinimalCsvValueLoggingModule2.VALUE_INDEX;
    }

    @Override
    public LocalDateTime toTime(String input) {
        return MinimalCsvValueLoggingModule2.toTime(input);
    }
}
