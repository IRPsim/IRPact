package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.eval.RunUntilFailureModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.InConsumerAgentModule2;
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
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.EVAL_EDGE_LABEL;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
public class InRunUntilFailureModule_evalgraphnode2 implements InConsumerAgentEvalModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL);
        setShapeColorFillBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_FILL, EVAL_BORDER);

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
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InRunUntilFailureModule input"}
            )
    )
    public InConsumerAgentModule2[] input_graphedge2;
    public InConsumerAgentModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentModule2 first) {
        this.input_graphedge2 = new InConsumerAgentModule2[]{first};
    }

    public InRunUntilFailureModule_evalgraphnode2() {
    }

    public InRunUntilFailureModule_evalgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InRunUntilFailureModule_evalgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRunUntilFailureModule_evalgraphnode2 newCopy(CopyCache cache) {
        InRunUntilFailureModule_evalgraphnode2 copy = new InRunUntilFailureModule_evalgraphnode2();
        return Dev.throwException();
    }

    @Override
    public RunUntilFailureModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        RunUntilFailureModule2 module = new RunUntilFailureModule2();
        module.setName(getName());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
