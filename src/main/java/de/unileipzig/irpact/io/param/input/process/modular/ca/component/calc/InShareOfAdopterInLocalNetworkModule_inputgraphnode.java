package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.filter.DisabledNodeFilterScheme;
import de.unileipzig.irpact.core.network.filter.NodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.ShareOfAdopterInLocalNetworkModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
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
@ToRemove
public class InShareOfAdopterInLocalNetworkModule_inputgraphnode implements InConsumerAgentCalculationModule {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_CALC_SHARELOCAL);
        setShapeColorBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_BORDER);

        addEntryWithDefault(res, thisClass(), "weight", VALUE_1);
        addEntry(res, thisClass(), "nodeFilterScheme");
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

    @FieldDefinition
    public InNodeDistanceFilterScheme[] nodeFilterScheme;
    public boolean hasNodeFilterScheme() {
        return ParamUtil.len(nodeFilterScheme) > 0;
    }
    public InNodeDistanceFilterScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }
    public void setNodeFilterScheme(InNodeDistanceFilterScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InNodeDistanceFilterScheme[0];
        } else {
            this.nodeFilterScheme = new InNodeDistanceFilterScheme[]{nodeFilterScheme};
        }
    }

    public InShareOfAdopterInLocalNetworkModule_inputgraphnode() {
    }

    @Override
    public InShareOfAdopterInLocalNetworkModule_inputgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InShareOfAdopterInLocalNetworkModule_inputgraphnode newCopy(CopyCache cache) {
        InShareOfAdopterInLocalNetworkModule_inputgraphnode copy = new InShareOfAdopterInLocalNetworkModule_inputgraphnode();
        return Dev.throwException();
    }

    @Override
    public ShareOfAdopterInLocalNetworkModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), ShareOfAdopterInLocalNetworkModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        ShareOfAdopterInLocalNetworkModule module = new ShareOfAdopterInLocalNetworkModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());

        if(hasNodeFilterScheme()) {
            InNodeDistanceFilterScheme inFilterScheme = getNodeFilterScheme();
            NodeFilterScheme filterScheme = parser.parseEntityTo(inFilterScheme);
            module.setNodeFilterScheme(filterScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set node filter scheme '{}'", filterScheme.getName());
        } else {
            module.setNodeFilterScheme(DisabledNodeFilterScheme.INSTANCE);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "no node filter scheme specified");
        }

        return module;
    }
}
