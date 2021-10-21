package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.UtilityEvaluatorModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
                label = EVALRA_LABEL,
                shape = EVALRA_SHAPE,
                color = EVALRA_COLOR,
                border = EVALRA_BORDER,
                tags = {EVALRA_GRAPHNODE}
        )
)
public class InUtilityEvaluatorModule2_evalragraphnode2 implements InConsumerAgentEvalRAModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_EVALRA_UTILITYEVAL);
        setShapeColorFillBorder(res, thisClass(), EVALRA_SHAPE, EVALRA_COLOR, EVALRA_FILL, EVALRA_BORDER);

        addEntry(res, thisClass(), "threshold_graphedge2");
        addEntry(res, thisClass(), "utility_graphedge2");
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
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InUtilityEvaluatorModule2 threshold"}
            )
    )
    public InConsumerAgentCalculationModule2[] threshold_graphedge2;
    public InConsumerAgentCalculationModule2 getThreshold() throws ParsingException {
        return ParamUtil.getInstance(threshold_graphedge2, "threshold");
    }
    public void setThreshold(InConsumerAgentCalculationModule2 threshold) {
        this.threshold_graphedge2 = new InConsumerAgentCalculationModule2[]{threshold};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InUtilityEvaluatorModule2 utility"}
            )
    )
    public InConsumerAgentCalculationModule2[] utility_graphedge2;
    public InConsumerAgentCalculationModule2 getUtility() throws ParsingException {
        return ParamUtil.getInstance(utility_graphedge2, "utility");
    }
    public void setUtility(InConsumerAgentCalculationModule2 utility) {
        this.utility_graphedge2 = new InConsumerAgentCalculationModule2[]{utility};
    }

    public InUtilityEvaluatorModule2_evalragraphnode2() {
    }

    @Override
    public InUtilityEvaluatorModule2_evalragraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUtilityEvaluatorModule2_evalragraphnode2 newCopy(CopyCache cache) {
        InUtilityEvaluatorModule2_evalragraphnode2 copy = new InUtilityEvaluatorModule2_evalragraphnode2();
        return Dev.throwException();
    }

    @Override
    public UtilityEvaluatorModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        UtilityEvaluatorModule2 module = new UtilityEvaluatorModule2();
        module.setName(getName());
        module.setThresholdModule(parser.parseEntityTo(getThreshold()));
        module.setDecisionMakingModule(parser.parseEntityTo(getUtility()));

        return module;
    }
}
