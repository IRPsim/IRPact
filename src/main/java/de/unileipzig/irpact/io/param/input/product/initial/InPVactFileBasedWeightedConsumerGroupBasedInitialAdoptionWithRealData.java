package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.product.handler.WeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
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
public class InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData implements InNewProductHandler {

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
        putClassPath(res, thisClass(), TreeViewStructure.PRODUCTS_INITADOPT_PVACTFILEWEIGHTEDCAGBASED);
        addEntryWithDefaultAndDomain(res, thisClass(), "scale", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "fixError", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "file");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public boolean scale = false;

    @FieldDefinition
    public boolean fixError = false;

    @FieldDefinition
    public InRealAdoptionDataFile[] file = new InRealAdoptionDataFile[0];

    public InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData() {
    }

    @Override
    public InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData newCopy(CopyCache cache) {
        InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData copy = new InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData();
        copy._name = _name;
        copy.file = cache.copyArray(file);
        return copy;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public boolean isScale() {
        return scale;
    }

    public void setFixError(boolean fixError) {
        this.fixError = fixError;
    }

    public boolean isFixError() {
        return fixError;
    }

    public void setFile(InRealAdoptionDataFile file) {
        this.file = new InRealAdoptionDataFile[]{file};
    }

    public InRealAdoptionDataFile getFile() throws ParsingException {
        return getInstance(file, "file");
    }

    @Override
    public WeightedConsumerGroupBasedInitialAdoptionWithRealData parse(IRPactInputParser parser) throws ParsingException {
        WeightedConsumerGroupBasedInitialAdoptionWithRealData handler = new WeightedConsumerGroupBasedInitialAdoptionWithRealData();
        handler.setName(getName());

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "WeightedConsumerGroupBasedInitialAdoptionWithRealData '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        handler.setRnd(rnd);

        SimulationEnvironment environment = parser.getEnvironment();
        ProductManager productManager = environment.getProducts();
        if(productManager.getNumberOfProductGroups() != 1) {
            if(parser.getRoot().getGeneral().runPVAct) {
                LOGGER.info("[{}] PVact enabled", getName());
            } else {
                throw new ParsingException("InPVactFileBasedConsumerGroupBasedInitialAdoption '{}' requires exactly one product group. Number of groups: {}", getName(), productManager.getNumberOfProductGroups());
            }
        }

        RealAdoptionData adoptionData = getFile().parse(parser);
        handler.setAdoptionData(adoptionData);

        handler.setZipAttributeName(RAConstants.ZIP);
        handler.setAgentFilter(ca -> {
            boolean isShare = ca.findAttribute(RAConstants.SHARE_1_2_HOUSE).asValueAttribute().getBooleanValue();
            boolean isPrivat = ca.findAttribute(RAConstants.HOUSE_OWNER).asValueAttribute().getBooleanValue();
            return isShare && isPrivat;
        });
        handler.setShareAttributeName(RAConstants.INITIAL_ADOPTER);
        handler.setScale(scale);
        handler.setFixError(fixError);

        return handler;
    }
}
