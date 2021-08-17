package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.initial.AttributeBasedInitialAdoption;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InPVactAttributeBasedInitialAdoption implements InInitialAdoptionHandler {

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
        putClassPath(res, thisClass(), InRootUI.PRODUCTS_INITADOPT_PVACTATTRBASED);
        addEntry(res, thisClass(), "placeholder");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public int placeholder;

    public InPVactAttributeBasedInitialAdoption() {
    }

    public InPVactAttributeBasedInitialAdoption(String name) {
        this._name = name;
    }

    @Override
    public InPVactAttributeBasedInitialAdoption copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactAttributeBasedInitialAdoption newCopy(CopyCache cache) {
        InPVactAttributeBasedInitialAdoption copy = new InPVactAttributeBasedInitialAdoption();
        copy._name = _name;
        return copy;
    }

    public String getName() {
        return _name;
    }

    @Override
    public AttributeBasedInitialAdoption parse(IRPactInputParser parser) throws ParsingException {
        AttributeBasedInitialAdoption handler = new AttributeBasedInitialAdoption();
        handler.setName(getName());
        handler.setAttributeName(RAConstants.INITIAL_ADOPTER);

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttributeBasedInitialAdoption '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        handler.setRnd(rnd);

        return handler;
    }
}
