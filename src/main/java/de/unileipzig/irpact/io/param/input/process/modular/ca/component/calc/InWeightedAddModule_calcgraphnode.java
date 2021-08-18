package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.WeightedAddModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = CALC_LABEL,
                shape = CALC_SHAPE,
                color = CALC_COLOR,
                border = CALC_BORDER,
                tags = {CALC_GRAPHNODE}
        )
)
public class InWeightedAddModule_calcgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTEDADD);
        setShapeColorBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_BORDER);

        addEntryWithDefault(res, thisClass(), "weight", VALUE_1);
        addEntryWithDefault(res, thisClass(), "weight1", VALUE_1);
        addEntry(res, thisClass(), "first_graphedge");
        addEntryWithDefault(res, thisClass(), "weight2", VALUE_1);
        addEntry(res, thisClass(), "second_graphedge");
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
    public double weight;
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @FieldDefinition
    public double weight1 = 1;
    public void setWeight1(double weight1) {
        this.weight1 = weight1;
    }
    public double getWeight1() {
        return weight1;
    }

    @FieldDefinition
    public double weight2 = 1;
    public void setWeight2(double weight2) {
        this.weight2 = weight2;
    }
    public double getWeight2() {
        return weight2;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InWeightedAddModule first"}
            )
    )
    public InConsumerAgentCalculationModule[] first_graphedge;
    public InConsumerAgentCalculationModule getFirst() throws ParsingException {
        return ParamUtil.getInstance(first_graphedge, "first");
    }
    public void setFirst(InConsumerAgentCalculationModule first) {
        this.first_graphedge = new InConsumerAgentCalculationModule[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InWeightedAddModule second"}
            )
    )
    public InConsumerAgentCalculationModule[] second_graphedge;
    public InConsumerAgentCalculationModule getSecond() throws ParsingException {
        return ParamUtil.getInstance(second_graphedge, "second");
    }
    public void setSecond(InConsumerAgentCalculationModule second) {
        this.second_graphedge = new InConsumerAgentCalculationModule[]{second};
    }

    public InWeightedAddModule_calcgraphnode() {
    }

    public InWeightedAddModule_calcgraphnode(String name, InConsumerAgentCalculationModule first, InConsumerAgentCalculationModule second) {
        setName(name);
        setFirst(first);
        setSecond(second);
    }

    @Override
    public InWeightedAddModule_calcgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InWeightedAddModule_calcgraphnode newCopy(CopyCache cache) {
        InWeightedAddModule_calcgraphnode copy = new InWeightedAddModule_calcgraphnode();
        return Dev.throwException();
    }

    @Override
    public WeightedAddModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), WeightedAddModule.class);
        }

        ConsumerAgentCalculationModule first = parser.parseEntityTo(getFirst());
        ConsumerAgentCalculationModule second = parser.parseEntityTo(getSecond());

        WeightedAddModule module = new WeightedAddModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());
        module.setWeight1(getWeight1());
        module.setWeight2(getWeight2());
        module.setModule1(first);
        module.setModule2(second);

        return module;
    }
}
