package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.SimpleGetPhaseModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

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
public class InSimpleGetPhaseModule_evalgraphnode implements InConsumerAgentEvaluationModule {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    protected static final String[] allPhaseFields = {"getAdopt", "getImpeded", "getInProcess"};
    protected static final XorWithoutUnselectRuleBuilder phaseBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(allPhaseFields);

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_SIMPLEGET);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntryWithDefaultAndDomain(res, thisClass(), "getAdopt", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "getImpeded", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "getInProcess", VALUE_TRUE, DOMAIN_BOOLEAN);

        setRules(res, thisClass(), allPhaseFields, phaseBuilder);
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
    public boolean getAdopt = false;
    @FieldDefinition
    public boolean getImpeded = true;
    @FieldDefinition
    public boolean getInProcess = false;

    public void setAdoptionResult(AdoptionResult result) {
        getAdopt = false;
        getImpeded = false;
        getInProcess = false;
        switch (result) {
            case ADOPTED:
                getAdopt = true;
                break;

            case IMPEDED:
                getImpeded = true;
                break;

            case IN_PROCESS:
                getInProcess = true;
                break;

            default:
                throw new IllegalStateException("unknown result: " + result);
        }
    }
    public AdoptionResult getAdoptionResult() throws ParsingException {
        List<AdoptionResult> units = new ArrayList<>();
        if(getAdopt) units.add(AdoptionResult.ADOPTED);
        if(getImpeded) units.add(AdoptionResult.IMPEDED);
        if(getInProcess) units.add(AdoptionResult.IN_PROCESS);

        switch(units.size()) {
            case 0:
                throw new ParsingException("Missing phase");

            case 1:
                return units.get(0);

            default:
                throw new ParsingException("Multiple phases: " + units);
        }
    }

    public InSimpleGetPhaseModule_evalgraphnode() {
    }

    @Override
    public InSimpleGetPhaseModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSimpleGetPhaseModule_evalgraphnode newCopy(CopyCache cache) {
        InSimpleGetPhaseModule_evalgraphnode copy = new InSimpleGetPhaseModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public SimpleGetPhaseModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), SimpleGetPhaseModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        SimpleGetPhaseModule module = new SimpleGetPhaseModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setAdoptionResult(getAdoptionResult());

        return module;
    }
}
