package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.DoAdoptModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
                label = EVALRA_LABEL,
                shape = EVALRA_SHAPE,
                color = EVALRA_COLOR,
                border = EVALRA_BORDER,
                tags = {EVALRA_GRAPHNODE}
        )
)
public class InDoAdoptModule_evalragraphnode2 implements InConsumerAgentEvalRAModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DOADOPT);
        setShapeColorFillBorder(res, thisClass(), EVALRA_SHAPE, EVALRA_COLOR, EVALRA_FILL, EVALRA_BORDER);

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
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InDoAdoptModule input"}
            )
    )
    public InConsumerAgentEvalRAModule2[] input_graphedge2;
    public InConsumerAgentEvalRAModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentEvalRAModule2 first) {
        this.input_graphedge2 = new InConsumerAgentEvalRAModule2[]{first};
    }

    public InDoAdoptModule_evalragraphnode2() {
    }

    public InDoAdoptModule_evalragraphnode2(String name) {
        setName(name);
    }

    @Override
    public InDoAdoptModule_evalragraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDoAdoptModule_evalragraphnode2 newCopy(CopyCache cache) {
        InDoAdoptModule_evalragraphnode2 copy = new InDoAdoptModule_evalragraphnode2();
        return Dev.throwException();
    }

    @Override
    public DoAdoptModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        DoAdoptModule2 module = new DoAdoptModule2();
        module.setName(getName());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
