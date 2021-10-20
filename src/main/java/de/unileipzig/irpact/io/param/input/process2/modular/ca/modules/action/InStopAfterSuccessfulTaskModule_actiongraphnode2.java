package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.action.StopAfterSuccessfulTaskModule2;
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
public class InStopAfterSuccessfulTaskModule_actiongraphnode2 implements InConsumerAgentActionModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_ACTION_STOP);
        setShapeColorBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_BORDER);

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

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = BOOL_EDGE_LABEL,
                    color = BOOL_EDGE_COLOR,
                    tags = {"InStopAfterSuccessfulTaskModule input"}
            )
    )
    public InConsumerAgentBoolModule2[] input_graphedge2;
    public InConsumerAgentBoolModule2[] getInput() throws ParsingException {
        return ParamUtil.getNonNullArray(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentBoolModule2... input) {
        this.input_graphedge2 = input;
    }

    public InStopAfterSuccessfulTaskModule_actiongraphnode2() {
    }

    @Override
    public InStopAfterSuccessfulTaskModule_actiongraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InStopAfterSuccessfulTaskModule_actiongraphnode2 newCopy(CopyCache cache) {
        InStopAfterSuccessfulTaskModule_actiongraphnode2 copy = new InStopAfterSuccessfulTaskModule_actiongraphnode2();
        return Dev.throwException();
    }

    @Override
    public StopAfterSuccessfulTaskModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        StopAfterSuccessfulTaskModule2<ConsumerAgentData2> module = new StopAfterSuccessfulTaskModule2<>();
        module.setName(getName());
        for(InConsumerAgentBoolModule2 submodule: getInput()) {
            module.add(parser.parseEntityTo(submodule));
        }

        return module;
    }
}
