package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AttributeModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
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
public class InAttributeInputModule_inputgraphnode2 implements InConsumerAgentInputModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_INPUT_ATTR);
        setShapeColorFillBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_COLOR, INPUT_BORDER);

        addEntry(res, thisClass(), "attribute");
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
    public InAttributeName[] attribute;
    public void setAttribute(InAttributeName attribute) {
        this.attribute = new InAttributeName[]{attribute};
    }
    public InAttributeName getAttribute() throws ParsingException {
        return ParamUtil.getInstance(attribute, "attribute");
    }
    public String getAttributeName() throws ParsingException {
        return getAttribute().getName();
    }

    public InAttributeInputModule_inputgraphnode2() {
    }

    public InAttributeInputModule_inputgraphnode2(String name) {
        setName(name);
    }

    public InAttributeInputModule_inputgraphnode2(String name, InAttributeName attribute) {
        setName(name);
        setAttribute(attribute);
    }

    @Override
    public InAttributeInputModule_inputgraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAttributeInputModule_inputgraphnode2 newCopy(CopyCache cache) {
        InAttributeInputModule_inputgraphnode2 copy = new InAttributeInputModule_inputgraphnode2();
        return Dev.throwException();
    }

    @Override
    public AttributeModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        AttributeModule2 module = new AttributeModule2();
        module.setName(getName());
        module.setAttributeName(getAttributeName());

        return module;
    }
}
