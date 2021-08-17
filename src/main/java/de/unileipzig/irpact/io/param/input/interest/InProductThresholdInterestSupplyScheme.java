package de.unileipzig.irpact.io.param.input.interest;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductThresholdInterestSupplyScheme implements InProductInterestSupplyScheme {

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
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_INTEREST, thisName());
        addEntry(res, thisClass(), "entries");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InProductGroupThresholdEntry[] entries;

    public InProductThresholdInterestSupplyScheme() {
    }

    @Override
    public InProductThresholdInterestSupplyScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InProductThresholdInterestSupplyScheme newCopy(CopyCache cache) {
        InProductThresholdInterestSupplyScheme copy = new InProductThresholdInterestSupplyScheme();
        copy._name = _name;
        copy.entries = cache.copyArray(entries);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setEntries(InProductGroupThresholdEntry[] entries) {
        this.entries = entries;
    }

    public InProductGroupThresholdEntry[] getEntries() throws ParsingException {
        return ParamUtil.getNonEmptyArray(entries, "entries");
    }

    @Override
    public ProductThresholdInterestSupplyScheme parse(IRPactInputParser parser) throws ParsingException {
        ProductThresholdInterestSupplyScheme interest = new ProductThresholdInterestSupplyScheme();
        interest.setName(getName());

        for(InProductGroupThresholdEntry entry: getEntries()) {
            InUnivariateDoubleDistribution inDist = entry.getDistribution();
            UnivariateDoubleDistribution dist = parser.parseEntityTo(inDist);

            for(InProductGroup inPg: entry.getProductGroups()) {
                ProductGroup pg = parser.parseEntityTo(inPg);

                if(interest.hasThresholdDistribution(pg)) {
                    throw ExceptionUtil.create(ParsingException::new, "product interest '{}' already has product group '{}'", interest.getName(), pg.getName());
                }

                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add product group '{}' with distribution '{}' to product interest '{}'", pg.getName(), dist.getName(), interest.getName());
                interest.setThresholdDistribution(pg, dist);
            }
        }

        return interest;
    }
}
