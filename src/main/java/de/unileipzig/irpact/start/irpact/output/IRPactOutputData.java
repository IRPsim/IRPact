package de.unileipzig.irpact.start.irpact.output;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.distribution.RandomBoundedDistribution;
import de.unileipzig.irpact.start.irpact.input.distribution.RandomDistribution;
import de.unileipzig.irpact.start.irpact.input.distribution.UnivariateDistribution;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroup;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroupAttribute;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class IRPactOutputData {

    public static final List<ParserInput> LIST = CollectionUtil.arrayListOf(
            ParserInput.newInstance(Type.REFERENCE, AgentGroup.class),
            ParserInput.newInstance(Type.REFERENCE, RandomBoundedDistribution.class),
            ParserInput.newInstance(Type.REFERENCE, RandomDistribution.class),
            ParserInput.newInstance(Type.REFERENCE, UnivariateDistribution.class),
            ParserInput.newInstance(Type.REFERENCE, Need.class),
            ParserInput.newInstance(Type.REFERENCE, FixedProduct.class),
            ParserInput.newInstance(Type.REFERENCE, ProductGroup.class),
            ParserInput.newInstance(Type.REFERENCE, ProductGroupAttribute.class),

            ParserInput.newInstance(Type.OUTPUT, AdaptionRate.class),
            ParserInput.newInstance(Type.OUTPUT, IRPactOutputData.class)
    );

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public ProductGroup[] productGroups;

    @FieldDefinition
    public FixedProduct[] fixedProducts;

    @FieldDefinition
    public Need[] needs;

    @FieldDefinition
    public AdaptionRate[] adaptionRates;

    public IRPactOutputData() {
    }

    public IRPactOutputData(
            AgentGroup[] agentGroups,
            ProductGroup[] productGroups,
            FixedProduct[] fixedProducts,
            Need[] needs,
            AdaptionRate[] adaptionRates) {
        this.agentGroups = agentGroups;
        this.productGroups = productGroups;
        this.fixedProducts = fixedProducts;
        this.needs = needs;
        this.adaptionRates = adaptionRates;
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRPactOutputData that = (IRPactOutputData) o;
        return Arrays.equals(agentGroups, that.agentGroups) &&
                Arrays.equals(productGroups, that.productGroups) &&
                Arrays.equals(fixedProducts, that.fixedProducts) &&
                Arrays.equals(needs, that.needs) &&
                Arrays.equals(adaptionRates, that.adaptionRates);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(agentGroups);
        result = 31 * result + Arrays.hashCode(productGroups);
        result = 31 * result + Arrays.hashCode(fixedProducts);
        result = 31 * result + Arrays.hashCode(needs);
        result = 31 * result + Arrays.hashCode(adaptionRates);
        return result;
    }

    @Override
    public String toString() {
        return "IRPactOutputData{" +
                "agentGroups=" + Arrays.toString(agentGroups) +
                ", productGroups=" + Arrays.toString(productGroups) +
                ", fixedProducts=" + Arrays.toString(fixedProducts) +
                ", needs=" + Arrays.toString(needs) +
                ", adaptionRates=" + Arrays.toString(adaptionRates) +
                '}';
    }
}
