package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.LogisticModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = CALC_LABEL,
                shape = CALC_SHAPE,
                color = CALC_COLOR,
                border = CALC_BORDER,
                tags = {CALC_GRAPHNODE}
        )
)
public class InLogisticModule_calcgraphnode2 implements InConsumerAgentCalculationModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC);
        setShapeColorFillBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_FILL, CALC_BORDER);

        addEntryWithDefault(res, thisClass(), "valueL", VALUE_1);
        addEntryWithDefault(res, thisClass(), "valueK", VALUE_1);
        addEntry(res, thisClass(), "xinput_graphedge2");
        addEntry(res, thisClass(), "x0input_graphedge2");
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
    public double valueL;
    public void setValueL(double valueL) {
        this.valueL = valueL;
    }
    public double getValueL() {
        return valueL;
    }

    @FieldDefinition
    public double valueK;
    public void setValueK(double valueK) {
        this.valueK = valueK;
    }
    public double getValueK() {
        return valueK;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InLogisticModule xinput"}
            )
    )
    public InConsumerAgentCalculationModule2[] xinput_graphedge2;
    public InConsumerAgentCalculationModule2 getXInput() throws ParsingException {
        return ParamUtil.getInstance(xinput_graphedge2, "x");
    }
    public void setXInput(InConsumerAgentCalculationModule2 first) {
        this.xinput_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InLogisticModule x0input"}
            )
    )
    public InConsumerAgentCalculationModule2[] x0input_graphedge2;
    public InConsumerAgentCalculationModule2 getX0Input() throws ParsingException {
        return ParamUtil.getInstance(x0input_graphedge2, "x0");
    }
    public void setX0Input(InConsumerAgentCalculationModule2 first) {
        this.x0input_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    public InLogisticModule_calcgraphnode2() {
    }

    public InLogisticModule_calcgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InLogisticModule_calcgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InLogisticModule_calcgraphnode2 newCopy(CopyCache cache) {
        InLogisticModule_calcgraphnode2 copy = new InLogisticModule_calcgraphnode2();
        return Dev.throwException();
    }

    @Override
    public LogisticModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        LogisticModule2<ConsumerAgentData2> module = new LogisticModule2<>();
        module.setName(getName());
        module.setL(getValueL());
        module.setK(getValueK());
        module.setX(parser.parseEntityTo(getXInput()));
        module.setX0(parser.parseEntityTo(getX0Input()));

        return module;
    }
}
