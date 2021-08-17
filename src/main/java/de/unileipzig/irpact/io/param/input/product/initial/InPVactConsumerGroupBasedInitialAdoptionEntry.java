package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.initial.AttributeBasedInitialAdoption;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
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
public class InPVactConsumerGroupBasedInitialAdoptionEntry implements InInitialAdoptionHandler {

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
        putClassPath(res, thisClass(), InRootUI.PRODUCTS_INITADOPT_PVACTCAGBASED_ENTRY);
        addEntries(res, thisClass(), "share", "cags", "zips");

        setDefault(res, thisClass(), "share", VALUE_ZERO);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double share;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InAttributeName[] zips;

    public InPVactConsumerGroupBasedInitialAdoptionEntry() {
    }

    public InPVactConsumerGroupBasedInitialAdoptionEntry(String name) {
        this._name = name;
    }

    @Override
    public InPVactConsumerGroupBasedInitialAdoptionEntry copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactConsumerGroupBasedInitialAdoptionEntry newCopy(CopyCache cache) {
        InPVactConsumerGroupBasedInitialAdoptionEntry copy = new InPVactConsumerGroupBasedInitialAdoptionEntry();
        copy._name = _name;
        copy.share = share;
        copy.cags = cache.copyArray(cags);
        copy.zips = cache.copyArray(zips);
        return copy;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public void setShare(double share) {
        this.share = share;
    }

    public double getShare() {
        return share;
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return getNonEmptyArray(cags, "cags");
    }

    public void setZips(InAttributeName[] zips) {
        this.zips = zips;
    }

    public InAttributeName[] getZips() throws ParsingException {
        return getNonEmptyArray(zips, "zips");
    }

    @Override
    public AttributeBasedInitialAdoption parse(IRPactInputParser parser) throws ParsingException {
        throw new UnsupportedOperationException();
    }
}
