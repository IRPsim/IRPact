package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.product.initial.ConsumerGroupBasedInitialAdoption;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
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
public class InPVactConsumerGroupBasedInitialAdoption implements InInitialAdoptionHandler {

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
        putClassPath(res, thisClass(), InRootUI.PRODUCTS_INITADOPT_PVACTCAGBASED);
        addEntry(res, thisClass(), "entries");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InPVactConsumerGroupBasedInitialAdoptionEntry[] entries;

    public InPVactConsumerGroupBasedInitialAdoption() {
    }

    public InPVactConsumerGroupBasedInitialAdoption(String name) {
        this._name = name;
    }

    @Override
    public InPVactConsumerGroupBasedInitialAdoption copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactConsumerGroupBasedInitialAdoption newCopy(CopyCache cache) {
        InPVactConsumerGroupBasedInitialAdoption copy = new InPVactConsumerGroupBasedInitialAdoption();
        copy._name = _name;
        copy.entries = cache.copyArray(entries);
        return copy;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setEntries(InPVactConsumerGroupBasedInitialAdoptionEntry[] entries) {
        this.entries = entries;
    }

    public InPVactConsumerGroupBasedInitialAdoptionEntry[] getEntries() throws ParsingException {
        return getNonEmptyArray(entries, "entries");
    }

    @Override
    public ConsumerGroupBasedInitialAdoption parse(IRPactInputParser parser) throws ParsingException {
        ConsumerGroupBasedInitialAdoption handler = new ConsumerGroupBasedInitialAdoption();
        handler.setName(getName());

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "ConsumerGroupBasedInitialAdoption '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        handler.setRnd(rnd);

        SimulationEnvironment environment = parser.getEnvironment();
        ProductManager productManager = environment.getProducts();
        if(productManager.getNumberOfProductGroups() != 1) {
            throw new ParsingException("InPVactConsumerGroupBasedInitialAdoption '{}' requires exactly one product group. Number of groups: {}", getName(), productManager.getNumberOfProductGroups());
        }

        ProductGroup pg = CollectionUtil.getFirst(productManager.getGroups());
        handler.register(pg, RAConstants.DOM_MILIEU, RAConstants.ZIP, RAConstants.SHARE_1_2_HOUSE);
        LOGGER.trace(
                IRPSection.INITIALIZATION_PARAMETER,
                "register [pg={}, cagKey={}, zipKey={}, validationKey={}] to handler '{}'",
                pg.getName(), RAConstants.DOM_MILIEU, RAConstants.ZIP, RAConstants.SHARE_1_2_HOUSE, getName()
        );
        for(InPVactConsumerGroupBasedInitialAdoptionEntry entry: getEntries()) {
            double share = entry.getShare();
            for(InConsumerAgentGroup inCag: entry.getConsumerAgentGroups()) {
                //cagName == DOM_MILIEU
                String cagName = inCag.getName();
                for(InAttributeName zips: entry.getZips()) {
                    String zipName = zips.getName();
                    LOGGER.trace(
                            IRPSection.INITIALIZATION_PARAMETER,
                            "add adoptioninfo [pg={}, cag={}, zip={}, share={}] to handler '{}'",
                            pg.getName(), cagName, zipName, share, getName()
                    );
                    handler.setShare(pg, cagName, zipName, share);
                }
            }
        }

        return handler;
    }
}
