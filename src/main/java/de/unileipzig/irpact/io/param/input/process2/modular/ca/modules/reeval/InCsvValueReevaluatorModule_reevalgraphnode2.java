package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval;

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

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = REEVAL_LABEL,
                shape = REEVAL_SHAPE,
                color = REEVAL_COLOR,
                border = REEVAL_BORDER,
                tags = {REEVAL_GRAPHNODE}
        )
)
public class InCsvValueReevaluatorModule_reevalgraphnode2 implements InConsumerAgentReevaluationModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_REEVAL_CSV);
        setShapeColorFillBorder(res, thisClass(), REEVAL_SHAPE, REEVAL_COLOR, REEVAL_FILL, REEVAL_BORDER);

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
                    tags = {"InCsvValueReevaluatorModule input"}
            )
    )
    public InConsumerAgentCalculationModule2[] input_graphedge2;
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.input_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    public InCsvValueReevaluatorModule_reevalgraphnode2() {
    }

    @Override
    public InCsvValueReevaluatorModule_reevalgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCsvValueReevaluatorModule_reevalgraphnode2 newCopy(CopyCache cache) {
        InCsvValueReevaluatorModule_reevalgraphnode2 copy = new InCsvValueReevaluatorModule_reevalgraphnode2();
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
        try {
            module.setDir(parser.getOptions().getCreatedDownloadDir());
        } catch (IOException e) {
            throw new ParsingException(e);
        }
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
