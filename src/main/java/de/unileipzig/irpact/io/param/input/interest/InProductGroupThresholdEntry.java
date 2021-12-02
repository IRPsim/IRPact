package de.unileipzig.irpact.io.param.input.interest;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY)
public class InProductGroupThresholdEntry implements InIRPactEntity {

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

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] interestDistribution = new InUnivariateDoubleDistribution[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InProductGroup[] productGroups = new InProductGroup[0];

    public InProductGroupThresholdEntry() {
    }

    @Override
    public InProductGroupThresholdEntry copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InProductGroupThresholdEntry newCopy(CopyCache cache) {
        InProductGroupThresholdEntry copy = new InProductGroupThresholdEntry();
        copy.name = name;
        copy.interestDistribution = cache.copyArray(interestDistribution);
        copy.productGroups = cache.copyArray(productGroups);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
