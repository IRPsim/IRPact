package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.SumThresholdEvaluationModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
public class InThresholdEvaluationModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_THRESH);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntryWithDefault(res, thisClass(), "threshold", VALUE_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "acceptIfBelowThreshold", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "adoptIfAccepted", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "impededIfFailed", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "input_graphedge");
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
    public double threshold;
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @FieldDefinition
    public boolean acceptIfBelowThreshold = true;
    public void setAcceptIfBelowThreshold(boolean acceptIfBelowThreshold) {
        this.acceptIfBelowThreshold = acceptIfBelowThreshold;
    }
    public boolean isAcceptIfBelowThreshold() {
        return acceptIfBelowThreshold;
    }

    @FieldDefinition
    public boolean adoptIfAccepted = true;
    public void setAdoptIfAccepted(boolean adoptIfAccepted) {
        this.adoptIfAccepted = adoptIfAccepted;
    }
    public boolean isAdoptIfAccepted() {
        return adoptIfAccepted;
    }

    @FieldDefinition
    public boolean impededIfFailed = true;
    public void setImpededIfFailed(boolean impededIfFailed) {
        this.impededIfFailed = impededIfFailed;
    }
    public boolean isImpededIfFailed() {
        return impededIfFailed;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InThresholdEvaluationModule input"}
            )
    )
    public InConsumerAgentCalculationModule[] input_graphedge;
    public void setInput(InConsumerAgentCalculationModule module) {
        this.input_graphedge = new InConsumerAgentCalculationModule[]{module};
    }
    public InConsumerAgentCalculationModule getInput() throws ParsingException {
        return ParamUtil.getInstance(input_graphedge, "input");
    }

    public InThresholdEvaluationModule_evalgraphnode() {
    }

    @Override
    public InThresholdEvaluationModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InThresholdEvaluationModule_evalgraphnode newCopy(CopyCache cache) {
        InThresholdEvaluationModule_evalgraphnode copy = new InThresholdEvaluationModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public SumThresholdEvaluationModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), SumThresholdEvaluationModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        SumThresholdEvaluationModule module = new SumThresholdEvaluationModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setThreshold(getThreshold());
        module.setAcceptIfBelowThreshold(isAcceptIfBelowThreshold());
        module.setAdoptIfAccepted(isAdoptIfAccepted());
        module.setImpededIfFailed(isImpededIfFailed());
        module.addSubModule(parser.parseEntityTo(getInput()));

        return module;
    }
}