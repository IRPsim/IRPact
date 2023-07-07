package de.unileipzig.irpact.io.param.input.process2.modular.components.init.general;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.handler.LinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.InInitializationHandler;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR3_HANDLER_INIT_LINPERATTR;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODULAR3_HANDLER_INIT_LINPERATTR)
public class InLinearePercentageAgentAttributeScaler implements InInitializationHandler {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = InitializationHandler.NORM_PRIORITY
    )
    public int priority = InitializationHandler.NORM_PRIORITY;
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 1
    )
    public double mValue;
    public double getM() {
        return mValue;
    }
    public void setM(double mValue) {
        this.mValue = mValue;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double nValue;
    public double getN() {
        return nValue;
    }
    public void setN(double nValue) {
        this.nValue = nValue;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
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

    @Override
    public InLinearePercentageAgentAttributeScaler copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InLinearePercentageAgentAttributeScaler newCopy(CopyCache cache) {
        InLinearePercentageAgentAttributeScaler copy = new InLinearePercentageAgentAttributeScaler();
        return Dev.throwException();
    }

    @Override
    public LinearePercentageAgentAttributeScaler parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}", thisName(), getName());

        LinearePercentageAgentAttributeScaler scaler = new LinearePercentageAgentAttributeScaler();
        scaler.setName(getName());
        scaler.setPriority(getPriority());
        scaler.setM(getM());
        scaler.setN(getN());
        scaler.setAttributeName(getAttributeName());

        return scaler;
    }
}
