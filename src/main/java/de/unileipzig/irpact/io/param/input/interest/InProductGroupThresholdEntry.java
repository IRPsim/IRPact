package de.unileipzig.irpact.io.param.input.interest;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductGroupThresholdEntry implements InIRPactEntity {

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
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_INTEREST, InProductThresholdInterestSupplyScheme.thisName(), thisName());
        addEntry(res, thisClass(), "interestDistribution");
        addEntry(res, thisClass(), "productGroups");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] interestDistribution;

    @FieldDefinition
    public InProductGroup[] productGroups;

    public InProductGroupThresholdEntry() {
    }

    @Override
    public InProductGroupThresholdEntry copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InProductGroupThresholdEntry newCopy(CopyCache cache) {
        InProductGroupThresholdEntry copy = new InProductGroupThresholdEntry();
        copy._name = _name;
        copy.interestDistribution = cache.copyArray(interestDistribution);
        copy.productGroups = cache.copyArray(productGroups);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(interestDistribution, "interestDistribution");
    }

    public void setDistribution(InUnivariateDoubleDistribution interestDistribution) {
        this.interestDistribution = new InUnivariateDoubleDistribution[]{interestDistribution};
    }

    public InProductGroup[] getProductGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(productGroups, "productGroups");
    }

    public void setProductGroups(InProductGroup[] productGroups) {
        this.productGroups = productGroups;
    }
}
