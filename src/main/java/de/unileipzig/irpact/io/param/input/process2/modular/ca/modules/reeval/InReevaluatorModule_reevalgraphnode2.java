package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.modules.action.ReevaluatorModule;
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
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.EVAL_EDGE_COLOR;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = REEVAL_LABEL,
                shape = REEVAL_SHAPE,
                color = REEVAL_COLOR,
                border = REEVAL_BORDER,
                tags = {REEVAL_GRAPHNODE}
        )
)
public class InReevaluatorModule_reevalgraphnode2 implements InConsumerAgentReevaluationModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL);
        setShapeColorFillBorder(res, thisClass(), REEVAL_SHAPE, REEVAL_COLOR, REEVAL_FILL, REEVAL_BORDER);

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
                    tags = {"InReevaluatorModule input"}
            )
    )
    public InConsumerAgentModule2[] input_graphedge2;
    public InConsumerAgentModule2[] getInput() throws ParsingException {
        return ParamUtil.getNonNullArray(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentModule2... input) {
        this.input_graphedge2 = input;
    }

    public InReevaluatorModule_reevalgraphnode2() {
    }

    public InReevaluatorModule_reevalgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InReevaluatorModule_reevalgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InReevaluatorModule_reevalgraphnode2 newCopy(CopyCache cache) {
        InReevaluatorModule_reevalgraphnode2 copy = new InReevaluatorModule_reevalgraphnode2();
        return Dev.throwException();
    }

    @Override
    public ReevaluatorModule<?> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse reevaluator {} '{}", thisName(), getName());

        ReevaluatorModule<?> reevaluator = new ReevaluatorModule<>();
        reevaluator.setName(getName());
        for(InConsumerAgentModule2 module: getInput()) {
            reevaluator.addSubmodule(parser.parseEntityTo(module));
        }

        return reevaluator;
    }
}
