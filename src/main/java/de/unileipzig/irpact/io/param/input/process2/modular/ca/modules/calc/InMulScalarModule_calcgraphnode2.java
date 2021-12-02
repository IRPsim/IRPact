package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.MulScalarModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
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
public class InMulScalarModule_calcgraphnode2 implements InConsumerAgentCalculationModule2 {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_MULSCALAR);
        setShapeColorFillBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_FILL, CALC_BORDER);

        addEntryWithDefault(res, thisClass(), "scalar", VALUE_1);
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
    public double scalar;
    public void setScalar(double scalar) {
        this.scalar = scalar;
    }
    public double getScalar() {
        return scalar;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InMulScalarModule input"}
            )
    )
    public InConsumerAgentCalculationModule2[] input_graphedge2;
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.input_graphedge2 = new InConsumerAgentCalculationModule2[]{first};
    }

    public InMulScalarModule_calcgraphnode2() {
    }

    public InMulScalarModule_calcgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InMulScalarModule_calcgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMulScalarModule_calcgraphnode2 newCopy(CopyCache cache) {
        InMulScalarModule_calcgraphnode2 copy = new InMulScalarModule_calcgraphnode2();
        return Dev.throwException();
    }

    @Override
    public MulScalarModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        MulScalarModule2<ConsumerAgentData2> module = new MulScalarModule2<>();
        module.setName(getName());
        module.setScalar(getScalar());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
