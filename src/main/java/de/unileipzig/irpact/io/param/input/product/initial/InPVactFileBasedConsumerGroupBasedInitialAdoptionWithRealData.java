package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.product.handler.ConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.getInstance;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_INITADOPT_PVACTFILECAGBASED;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_INITADOPT_PVACTFILECAGBASED)
public class InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData implements InNewProductHandler {

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

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InRealAdoptionDataFile[] file = new InRealAdoptionDataFile[0];

    public InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData() {
    }

    @Override
    public InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData newCopy(CopyCache cache) {
        InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData copy = new InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData();
        copy.name = name;
        copy.file = cache.copyArray(file);
        return copy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFile(InRealAdoptionDataFile file) {
        this.file = new InRealAdoptionDataFile[]{file};
    }

    public InRealAdoptionDataFile getFile() throws ParsingException {
        return getInstance(file, "file");
    }

    @Override
    public ConsumerGroupBasedInitialAdoptionWithRealData parse(IRPactInputParser parser) throws ParsingException {
        ConsumerGroupBasedInitialAdoptionWithRealData handler = new ConsumerGroupBasedInitialAdoptionWithRealData();
        handler.setName(getName());

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "ConsumerGroupBasedInitialAdoption '{}' uses seed: {}", getName(), rnd.getInitialSeed());
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
        handler.setValidationAttributeName(RAConstants.SHARE_1_2_HOUSE);
        handler.setShareAttributeName(RAConstants.INITIAL_ADOPTER);

        return handler;
    }
}
