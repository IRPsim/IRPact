package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.bool.ThresholdReachedModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
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
                label = BOOL_LABEL,
                shape = BOOL_SHAPE,
                color = BOOL_COLOR,
                border = BOOL_BORDER,
                tags = {BOOL_GRAPHNODE}
        )
)
public class InThresholdReachedModule_boolgraphnode2 implements InConsumerAgentBoolModule2 {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD);
        setShapeColorFillBorder(res, thisClass(), BOOL_SHAPE, BOOL_COLOR, BOOL_FILL, BOOL_BORDER);

        addEntryWithDefault(res, thisClass(), "priority", asValue(Module2.NORM_PRIORITY));
        addEntry(res, thisClass(), "draw_graphedge2");
        addEntry(res, thisClass(), "threshold_graphedge2");
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
    public int priority = Module2.NORM_PRIORITY;
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return priority;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InThresholdReachedModule draw"}
            )
    )
    public InConsumerAgentCalculationModule2[] draw_graphedge2;
    public InConsumerAgentCalculationModule2 getDraw() throws ParsingException {
        return ParamUtil.getInstance(draw_graphedge2, "draw");
    }
    public void setDraw(InConsumerAgentCalculationModule2 value) {
        this.draw_graphedge2 = new InConsumerAgentCalculationModule2[]{value};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InThresholdReachedModule threshold"}
            )
    )
    public InConsumerAgentCalculationModule2[] threshold_graphedge2;
    public InConsumerAgentCalculationModule2 getThreshold() throws ParsingException {
        return ParamUtil.getInstance(threshold_graphedge2, "threshold");
    }
    public void setThreshold(InConsumerAgentCalculationModule2 threshold) {
        this.threshold_graphedge2 = new InConsumerAgentCalculationModule2[]{threshold};
    }

    public InThresholdReachedModule_boolgraphnode2() {
    }

    public InThresholdReachedModule_boolgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InThresholdReachedModule_boolgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InThresholdReachedModule_boolgraphnode2 newCopy(CopyCache cache) {
        InThresholdReachedModule_boolgraphnode2 copy = new InThresholdReachedModule_boolgraphnode2();
        return Dev.throwException();
    }

    @Override
    public ThresholdReachedModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        ThresholdReachedModule2<ConsumerAgentData2> module = new ThresholdReachedModule2<>();
        module.setName(getName());
        module.setPriority(getPriority());
        module.setDrawModule(parser.parseEntityTo(getDraw()));
        module.setThresholdModule(parser.parseEntityTo(getThreshold()));

        return module;
    }
}
