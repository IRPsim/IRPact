package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.DecisionMakingDeciderModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.NewDecisionMakingDeciderModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InConsumerAgentBoolModule2;
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
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.EVALRA_EDGE_COLOR;

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
public class InNewDecisionMakingDeciderModule2_evalragraphnode2 implements InConsumerAgentEvalRAModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_EVALRA_NEWDECISIONDECIDER);
        setShapeColorFillBorder(res, thisClass(), EVALRA_SHAPE, EVALRA_COLOR, EVALRA_FILL, EVALRA_BORDER);

        addEntryWithDefaultAndDomain(res, thisClass(), "forceEvaluation", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "finCheck_graphedge2");
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

    @FieldDefinition
    public boolean forceEvaluation = false;
    public boolean isForceEvaluation() {
        return forceEvaluation;
    }
    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = BOOL_EDGE_LABEL,
                    color = BOOL_EDGE_COLOR,
                    tags = {"InNewDecisionMakingDeciderModule2 finCheck"}
            )
    )
    public InConsumerAgentBoolModule2[] finCheck_graphedge2;
    public InConsumerAgentBoolModule2 getFinCheck() throws ParsingException {
        return ParamUtil.getInstance(finCheck_graphedge2, "finCheck");
    }
    public void setFinCheck(InConsumerAgentBoolModule2 finCheck) {
        this.finCheck_graphedge2 = new InConsumerAgentBoolModule2[]{finCheck};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InNewDecisionMakingDeciderModule2 utility"}
            )
    )
    public InConsumerAgentEvalRAModule2[] utility_graphedge2;
    public InConsumerAgentEvalRAModule2 getUtility() throws ParsingException {
        return ParamUtil.getInstance(utility_graphedge2, "utility");
    }
    public void setUtility(InConsumerAgentEvalRAModule2 utility) {
        this.utility_graphedge2 = new InConsumerAgentEvalRAModule2[]{utility};
    }

    public InNewDecisionMakingDeciderModule2_evalragraphnode2() {
    }

    @Override
    public InNewDecisionMakingDeciderModule2_evalragraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNewDecisionMakingDeciderModule2_evalragraphnode2 newCopy(CopyCache cache) {
        InNewDecisionMakingDeciderModule2_evalragraphnode2 copy = new InNewDecisionMakingDeciderModule2_evalragraphnode2();
        return Dev.throwException();
    }

    @Override
    public NewDecisionMakingDeciderModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        NewDecisionMakingDeciderModule2 module = new NewDecisionMakingDeciderModule2();
        module.setName(getName());
        module.setForceEvaluation(isForceEvaluation());
        module.setFinancialCheckModule(parser.parseEntityTo(getFinCheck()));
        module.setUtilityModule(parser.parseEntityTo(getUtility()));

        return module;
    }
}
