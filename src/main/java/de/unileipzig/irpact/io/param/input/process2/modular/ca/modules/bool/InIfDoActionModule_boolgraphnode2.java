package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.bool.IfDoActionModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InConsumerAgentActionModule2;
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
public class InIfDoActionModule_boolgraphnode2 implements InConsumerAgentBoolModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_BOOL_IFDO);
        setShapeColorBorder(res, thisClass(), BOOL_SHAPE, BOOL_COLOR, BOOL_BORDER);

        addEntryWithDefault(res, thisClass(), "priority", asValue(Module2.NORM_PRIORITY));
        addEntry(res, thisClass(), "ifInput_graphedge2");
        addEntry(res, thisClass(), "taskInput_graphedge2");
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
                    label = BOOL_EDGE_LABEL,
                    color = BOOL_EDGE_COLOR,
                    tags = {"InIfDoActionModule ifInput"}
            )
    )
    public InConsumerAgentBoolModule2[] ifInput_graphedge2;
    public InConsumerAgentBoolModule2 getIfInput() throws ParsingException {
        return ParamUtil.getInstance(ifInput_graphedge2, "ifInput");
    }
    public void setIfInput(InConsumerAgentBoolModule2 first) {
        this.ifInput_graphedge2 = new InConsumerAgentBoolModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = ACTION_EDGE_LABEL,
                    color = ACTION_EDGE_COLOR,
                    tags = {"InIfDoActionModule taskInput"}
            )
    )
    public InConsumerAgentActionModule2[] taskInput_graphedge2;
    public InConsumerAgentActionModule2 getTaskInput() throws ParsingException {
        return ParamUtil.getInstance(taskInput_graphedge2, "taskInput");
    }
    public void setTaskInput(InConsumerAgentActionModule2 first) {
        this.taskInput_graphedge2 = new InConsumerAgentActionModule2[]{first};
    }

    public InIfDoActionModule_boolgraphnode2() {
    }

    @Override
    public InIfDoActionModule_boolgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InIfDoActionModule_boolgraphnode2 newCopy(CopyCache cache) {
        InIfDoActionModule_boolgraphnode2 copy = new InIfDoActionModule_boolgraphnode2();
        return Dev.throwException();
    }

    @Override
    public IfDoActionModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        IfDoActionModule2<ConsumerAgentData2> module = new IfDoActionModule2<>();
        module.setName(getName());
        module.setPriority(getPriority());
        module.setIfModule(parser.parseEntityTo(getIfInput()));
        module.setTaskModule(parser.parseEntityTo(getTaskInput()));

        return module;
    }
}
