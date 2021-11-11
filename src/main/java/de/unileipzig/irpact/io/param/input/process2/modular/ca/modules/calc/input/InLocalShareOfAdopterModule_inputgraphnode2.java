package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.filter.DisabledNodeFilterScheme;
import de.unileipzig.irpact.core.network.filter.NodeFilterScheme;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.LocalShareOfAdopterModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeFilterDistanceScheme;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
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
                label = INPUT_LABEL,
                shape = INPUT_SHAPE,
                color = INPUT_COLOR,
                border = INPUT_BORDER,
                tags = {INPUT_GRAPHNODE}
        )
)
public class InLocalShareOfAdopterModule_inputgraphnode2 implements InConsumerAgentInputModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_INPUT_LOCAL);
        setShapeColorFillBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_COLOR, INPUT_BORDER);

        addEntryWithDefault(res, thisClass(), "maxToStore", asValue(1000));
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
    public int maxToStore = 1000;
    public void setMaxToStore(int maxToStore) {
        this.maxToStore = maxToStore;
    }
    public int getMaxToStore() {
        return maxToStore;
    }

    @FieldDefinition
    public InNodeFilterDistanceScheme[] nodeFilterScheme;
    public boolean hasNodeFilterScheme() {
        return ParamUtil.len(nodeFilterScheme) > 0;
    }
    public InNodeFilterDistanceScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }
    public void setNodeFilterScheme(InNodeFilterDistanceScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InNodeFilterDistanceScheme[0];
        } else {
            this.nodeFilterScheme = new InNodeFilterDistanceScheme[]{nodeFilterScheme};
        }
    }

    public InLocalShareOfAdopterModule_inputgraphnode2() {
    }

    public InLocalShareOfAdopterModule_inputgraphnode2(String name) {
        setName(name);
    }

    @Override
    public InLocalShareOfAdopterModule_inputgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InLocalShareOfAdopterModule_inputgraphnode2 newCopy(CopyCache cache) {
        InLocalShareOfAdopterModule_inputgraphnode2 copy = new InLocalShareOfAdopterModule_inputgraphnode2();
        return Dev.throwException();
    }

    @Override
    public LocalShareOfAdopterModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        LocalShareOfAdopterModule2 module = new LocalShareOfAdopterModule2();
        module.setName(getName());
        module.setMaxToStore(maxToStore);
        if(hasNodeFilterScheme()) {
            InNodeFilterDistanceScheme inFilterScheme = getNodeFilterScheme();
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
