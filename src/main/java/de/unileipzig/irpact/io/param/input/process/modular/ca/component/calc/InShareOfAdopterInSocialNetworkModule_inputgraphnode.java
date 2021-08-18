package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.ShareOfAdopterInSocialNetworkModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
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
                label = INPUT_LABEL,
                shape = INPUT_SHAPE,
                color = INPUT_COLOR,
                border = INPUT_BORDER,
                tags = {INPUT_GRAPHNODE}
        )
)
public class InShareOfAdopterInSocialNetworkModule_inputgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_CALC_SHARESOCIAL);
        setShapeColorBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_BORDER);

        addEntryWithDefault(res, thisClass(), "weight", VALUE_1);
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
    public double weight;
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public InShareOfAdopterInSocialNetworkModule_inputgraphnode() {
    }

    @Override
    public InShareOfAdopterInSocialNetworkModule_inputgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InShareOfAdopterInSocialNetworkModule_inputgraphnode newCopy(CopyCache cache) {
        InShareOfAdopterInSocialNetworkModule_inputgraphnode copy = new InShareOfAdopterInSocialNetworkModule_inputgraphnode();
        return Dev.throwException();
    }

    @Override
    public ShareOfAdopterInSocialNetworkModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), ShareOfAdopterInSocialNetworkModule.class);
        }

        ShareOfAdopterInSocialNetworkModule module = new ShareOfAdopterInSocialNetworkModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());

        return module;
    }
}
