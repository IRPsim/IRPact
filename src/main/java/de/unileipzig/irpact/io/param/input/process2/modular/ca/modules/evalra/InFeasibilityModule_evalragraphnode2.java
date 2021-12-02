package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.FeasibilityModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
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
                label = EVALRA_LABEL,
                shape = EVALRA_SHAPE,
                color = EVALRA_COLOR,
                border = EVALRA_BORDER,
                tags = {EVALRA_GRAPHNODE}
        )
)
public class InFeasibilityModule_evalragraphnode2 implements InConsumerAgentEvalRAModule2 {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODEL4_PVACTMODULES_PVGENERAL_FEASIBILITY);
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
                    tags = {"InFeasibilityModule input"}
            )
    )
    public InConsumerAgentActionModule2[] input_graphedge2;
    public InConsumerAgentActionModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge2, "input");
    }
    public void setInput(InConsumerAgentActionModule2 first) {
        this.input_graphedge2 = new InConsumerAgentActionModule2[]{first};
    }

    public InFeasibilityModule_evalragraphnode2() {
    }

    public InFeasibilityModule_evalragraphnode2(String name) {
        setName(name);
    }

    @Override
    public InFeasibilityModule_evalragraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFeasibilityModule_evalragraphnode2 newCopy(CopyCache cache) {
        InFeasibilityModule_evalragraphnode2 copy = new InFeasibilityModule_evalragraphnode2();
        return Dev.throwException();
    }

    @Override
    public FeasibilityModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        FeasibilityModule2 module = new FeasibilityModule2();
        module.setName(getName());
        module.setActionModule(parser.parseEntityTo(getInput()));

        return module;
    }
}
