package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.WeightedModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
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
@ToRemove
public class InWeightedModule_calcgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTED);
        setShapeColorBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_BORDER);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "input_graphedge");

        setDefault(res, thisClass(), "weight", VALUE_1);
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

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InWeightedModule input"}
            )
    )
    public InConsumerAgentCalculationModule[] input_graphedge;
    public void setInput(InConsumerAgentCalculationModule input) {
        this.input_graphedge = new InConsumerAgentCalculationModule[]{input};
    }
    public InConsumerAgentCalculationModule getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge, "input");
    }

    public InWeightedModule_calcgraphnode() {
    }

    @Override
    public InWeightedModule_calcgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InWeightedModule_calcgraphnode newCopy(CopyCache cache) {
        InWeightedModule_calcgraphnode copy = new InWeightedModule_calcgraphnode();
        return Dev.throwException();
    }

    @Override
    public WeightedModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), WeightedModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        ConsumerAgentCalculationModule source = parser.parseEntityTo(getInput());

        WeightedModule module = new WeightedModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());
        module.setModule(source);

        return module;
    }
}
