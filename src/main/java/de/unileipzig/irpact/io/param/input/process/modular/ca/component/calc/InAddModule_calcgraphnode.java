package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.AddModule;
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
public class InAddModule_calcgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_CALC_ADD);
        setShapeColorBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_BORDER);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "first_graphedge");
        addEntry(res, thisClass(), "second_graphedge");

        setDefault(res, thisClass(), "weight", VALUE_ONE);
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
                    tags = {"InAddModule first"}
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
                    tags = {"InAddModule second"}
            )
    )
    public InConsumerAgentCalculationModule[] second_graphedge;
    public InConsumerAgentCalculationModule getSecond() throws ParsingException {
        return ParamUtil.getInstance(second_graphedge, "second");
    }
    public void setSecond(InConsumerAgentCalculationModule second) {
        this.second_graphedge = new InConsumerAgentCalculationModule[]{second};
    }

    public InAddModule_calcgraphnode() {
    }

    public InAddModule_calcgraphnode(String name, InConsumerAgentCalculationModule first, InConsumerAgentCalculationModule second) {
        setName(name);
        setFirst(first);
        setSecond(second);
    }

    @Override
    public InAddModule_calcgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAddModule_calcgraphnode newCopy(CopyCache cache) {
        InAddModule_calcgraphnode copy = new InAddModule_calcgraphnode();
        return Dev.throwException();
    }

    @Override
    public AddModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), AddModule.class);
        }

        ConsumerAgentCalculationModule first = parser.parseEntityTo(getFirst());
        ConsumerAgentCalculationModule second = parser.parseEntityTo(getSecond());

        AddModule module = new AddModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());
        module.setModule1(first);
        module.setModule2(second);

        return module;
    }
}
