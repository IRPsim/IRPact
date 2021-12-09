package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.handler.DefaultAwarenessInterestHandler;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_INITADOPT_PVACTATTRBASED;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_INITADOPT_PVACTATTRBASED)
public class InPVactDefaultAwarenessInterestHandler implements InNewProductHandler {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int priority = 0;
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return priority;
    }

    public InPVactDefaultAwarenessInterestHandler() {
    }

    @Override
    public InPVactDefaultAwarenessInterestHandler copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactDefaultAwarenessInterestHandler newCopy(CopyCache cache) {
        InPVactDefaultAwarenessInterestHandler copy = new InPVactDefaultAwarenessInterestHandler();
        copy.name = name;
        return copy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DefaultAwarenessInterestHandler parse(IRPactInputParser parser) throws ParsingException {
        DefaultAwarenessInterestHandler handler = new DefaultAwarenessInterestHandler();
        handler.setName(getName());
        handler.setAwarenessAttributeName(RAConstants.INITIAL_PRODUCT_AWARENESS);
        handler.setInterestAttributeName(RAConstants.INITIAL_PRODUCT_INTEREST);

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttributeBasedInitialAdoption '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        handler.setRnd(rnd);
        handler.setPriority(getPriority());

        return handler;
    }
}
