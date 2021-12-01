package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.action.IfElseActionModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InConsumerAgentBoolModule2;
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
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.ACTION_EDGE_LABEL;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = ACTION_LABEL,
                shape = ACTION_SHAPE,
                color = ACTION_COLOR,
                border = ACTION_BORDER,
                tags = {ACTION_GRAPHNODE}
        )
)
public class InIfElseActionModule_actiongraphnode2 implements InConsumerAgentActionModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE);
        setShapeColorFillBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_FILL, ACTION_BORDER);

        addEntry(res, thisClass(), "test_graphedge2");
        addEntry(res, thisClass(), "onTrue_graphedge2");
        addEntry(res, thisClass(), "onFalse_graphedge2");
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

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = BOOL_EDGE_LABEL,
                    color = BOOL_EDGE_COLOR,
                    tags = {"InIfElseActionModule test"}
            )
    )
    public InConsumerAgentBoolModule2[] test_graphedge2;
    public InConsumerAgentBoolModule2 getTestModule() throws ParsingException {
        return ParamUtil.getInstance(test_graphedge2, "test");
    }
    public void setTestModule(InConsumerAgentBoolModule2 test) {
        this.test_graphedge2 = new InConsumerAgentBoolModule2[]{test};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = ACTION_EDGE_LABEL,
                    color = ACTION_EDGE_COLOR,
                    tags = {"InIfElseActionModule onTrue"}
            )
    )
    public InConsumerAgentActionModule2[] onTrue_graphedge2;
    public InConsumerAgentActionModule2 getOnTrueModule() throws ParsingException {
        return ParamUtil.getInstance(onTrue_graphedge2, "onTrue");
    }
    public void setOnTrueModule(InConsumerAgentActionModule2 onTrue) {
        this.onTrue_graphedge2 = new InConsumerAgentActionModule2[]{onTrue};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = ACTION_EDGE_LABEL,
                    color = ACTION_EDGE_COLOR,
                    tags = {"InIfElseActionModule onFalse"}
            )
    )
    public InConsumerAgentActionModule2[] onFalse_graphedge2;
    public InConsumerAgentActionModule2 getOnFalseModule() throws ParsingException {
        return ParamUtil.getInstance(onFalse_graphedge2, "onFalse");
    }
    public void setOnFalseModule(InConsumerAgentActionModule2 onFalse) {
        this.onFalse_graphedge2 = new InConsumerAgentActionModule2[]{onFalse};
    }

    public InIfElseActionModule_actiongraphnode2() {
    }

    public InIfElseActionModule_actiongraphnode2(String name) {
        setName(name);
    }

    @Override
    public InIfElseActionModule_actiongraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InIfElseActionModule_actiongraphnode2 newCopy(CopyCache cache) {
        InIfElseActionModule_actiongraphnode2 copy = new InIfElseActionModule_actiongraphnode2();
        return Dev.throwException();
    }

    @Override
    public IfElseActionModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        IfElseActionModule2<ConsumerAgentData2> module = new IfElseActionModule2<>();
        module.setName(getName());
        module.setTest(parser.parseEntityTo(getTestModule()));
        module.setOnTrue(parser.parseEntityTo(getOnTrueModule()));
        module.setOnFalse(parser.parseEntityTo(getOnFalseModule()));

        return module;
    }
}
