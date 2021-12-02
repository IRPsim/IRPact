package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate.LinearePercentageAgentAttributeUpdater;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InLinearePercentageAgentAttributeUpdater implements InReevaluator2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_REEVAL_LINPERUPDATER);

        addEntryWithDefault(res, thisClass(), "priority", asValue(Reevaluator.NORM_PRIORITY));
        addEntry(res, thisClass(), "scaler");
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
    public int priority = Reevaluator.NORM_PRIORITY;
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @FieldDefinition
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
