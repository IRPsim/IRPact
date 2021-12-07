package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate.LinearePercentageAgentAttributeUpdater;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR3_REEVAL_LINPERUPDATER;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODULAR3_REEVAL_LINPERUPDATER)
public class InLinearePercentageAgentAttributeUpdater implements InReevaluator2 {

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
            intDefault = Reevaluator.NORM_PRIORITY
    )
    public int priority = Reevaluator.NORM_PRIORITY;
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InLinearePercentageAgentAttributeScaler[] scaler = new InLinearePercentageAgentAttributeScaler[0];
    public void setScaler(InLinearePercentageAgentAttributeScaler scaler) {
        this.scaler = new InLinearePercentageAgentAttributeScaler[]{scaler};
    }
    public InLinearePercentageAgentAttributeScaler getScaler() throws ParsingException {
        return getInstance(scaler, "scaler");
    }

    @Override
    public InLinearePercentageAgentAttributeUpdater copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InLinearePercentageAgentAttributeUpdater newCopy(CopyCache cache) {
        InLinearePercentageAgentAttributeUpdater copy = new InLinearePercentageAgentAttributeUpdater();
        return Dev.throwException();
    }

    @Override
    public LinearePercentageAgentAttributeUpdater parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}", thisName(), getName());

        LinearePercentageAgentAttributeUpdater updater = new LinearePercentageAgentAttributeUpdater();
        updater.setName(getName());
        updater.setPriority(getPriority());
        updater.setScaler(parser.parseEntityTo(getScaler()));

        return updater;
    }
}
