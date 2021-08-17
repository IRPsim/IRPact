package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.commons.util.fio2.Rows;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.product.initial.ConsumerGroupBasedInitialAdoption;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
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
public class InPVactFileBasedConsumerGroupBasedInitialAdoption implements InInitialAdoptionHandler {

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
        putClassPath(res, thisClass(), InRootUI.PRODUCTS_INITADOPT_PVACTFILECAGBASED);
        addEntry(res, thisClass(), "file");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InPVFile[] file = new InPVFile[0];

    public InPVactFileBasedConsumerGroupBasedInitialAdoption() {
    }

    @Override
    public InPVactFileBasedConsumerGroupBasedInitialAdoption copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactFileBasedConsumerGroupBasedInitialAdoption newCopy(CopyCache cache) {
        InPVactFileBasedConsumerGroupBasedInitialAdoption copy = new InPVactFileBasedConsumerGroupBasedInitialAdoption();
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

    public void setFile(InPVFile file) {
        this.file = new InPVFile[]{file};
    }

    public InPVFile getFile() throws ParsingException {
        return getInstance(file, "file");
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
            throw new ParsingException("InPVactFileBasedConsumerGroupBasedInitialAdoption '{}' requires exactly one product group. Number of groups: {}", getName(), productManager.getNumberOfProductGroups());
        }

        TypedMatrix<String, String, Double> data = loadFile(environment.getResourceLoader());

        ProductGroup pg = CollectionUtil.getFirst(productManager.getGroups());
        handler.register(pg, RAConstants.DOM_MILIEU, RAConstants.ZIP, RAConstants.SHARE_1_2_HOUSE);
        LOGGER.trace(
                IRPSection.INITIALIZATION_PARAMETER,
                "register [pg={}, cagKey={}, zipKey={}, validationKey={}] to handler '{}'",
                pg.getName(), RAConstants.DOM_MILIEU, RAConstants.ZIP, RAConstants.SHARE_1_2_HOUSE, getName()
        );

        for(String cagName: data.getM()) {
            for(String zipName: data.getN(cagName)) {
                double share = data.getOrDefault(cagName, zipName, -1.0);
                if(share == -1) {
                    throw new ParsingException("missing share for cag '{}' and zip '{}'", cagName, zipName);
                }

                LOGGER.trace(
                        IRPSection.INITIALIZATION_PARAMETER,
                        "add adoptioninfo [pg={}, cag={}, zip={}, share={}] to handler '{}'",
                        pg.getName(), cagName, zipName, share, getName()
                );
                handler.setShare(pg, cagName, zipName, share);
            }
        }

        return handler;
    }

    protected TypedMatrix<String, String, Double> loadFile(ResourceLoader loader) throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try loading input file");
        InPVFile file = getFile();
        String fileName = file.getFileNameWithoutExtension();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "loading file '{}'", fileName);
        Rows<Attribute> rows = parseXlsx(loader, fileName);
        return toDoubleMatrix(rows);
    }
}
