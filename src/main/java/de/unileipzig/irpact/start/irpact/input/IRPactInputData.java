package de.unileipzig.irpact.start.irpact.input;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.distribution.RandomBoundedDistribution;
import de.unileipzig.irpact.start.irpact.input.distribution.RandomDistribution;
import de.unileipzig.irpact.start.irpact.input.distribution.UnivariateDistribution;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroup;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroupAttribute;
import de.unileipzig.irpact.start.irpact.input.simulation.ContinousTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.DiscretTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.TimeModel;
import de.unileipzig.irptools.defstructure.GamsType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class IRPactInputData {

    public static final List<ParserInput> LIST = CollectionUtil.arrayListOf(
            ParserInput.newInstance(GamsType.INPUT, AgentGroup.class),
            ParserInput.newInstance(GamsType.INPUT, RandomBoundedDistribution.class),
            ParserInput.newInstance(GamsType.INPUT, RandomDistribution.class),
            ParserInput.newInstance(GamsType.INPUT, UnivariateDistribution.class),
            ParserInput.newInstance(GamsType.INPUT, Need.class),
            ParserInput.newInstance(GamsType.INPUT, FixedProduct.class),
            ParserInput.newInstance(GamsType.INPUT, ProductGroup.class),
            ParserInput.newInstance(GamsType.INPUT, ProductGroupAttribute.class),
            ParserInput.newInstance(GamsType.INPUT, ContinousTimeModel.class),
            ParserInput.newInstance(GamsType.INPUT, DiscretTimeModel.class),
            ParserInput.newInstance(GamsType.INPUT, TimeModel.class),
            ParserInput.newInstance(GamsType.INPUT, ScalarData.class),
            ParserInput.newInstance(GamsType.INPUT, IRPactInputData.class)
    );

    @FieldDefinition
    public ScalarData scalarData;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public TimeModel[] timeModels;

    @FieldDefinition
    public ProductGroup[] productGroups;

    @FieldDefinition
    public FixedProduct[] fixedProducts;

    public IRPactInputData() {
    }

    public IRPactInputData(
            ScalarData scalarData,
            AgentGroup[] agentGroups,
            TimeModel[] timeModels,
            ProductGroup[] productGroups,
            FixedProduct[] fixedProducts) {
        this.scalarData = scalarData;
        this.agentGroups = agentGroups;
        this.timeModels = timeModels;
        this.productGroups = productGroups;
        this.fixedProducts = fixedProducts;
    }

    //=========================
    //helper
    //=========================

    public long getDelay() {
        TimeModel tm = timeModels[0];
        if(tm instanceof ContinousTimeModel) {
            ContinousTimeModel ctm = (ContinousTimeModel) tm;
            return ctm.delay;
        } else {
            DiscretTimeModel dtm = (DiscretTimeModel) tm;
            return dtm.delay;
        }
    }

    public Set<FixedProduct> getProducts() {
        Set<FixedProduct> set = new LinkedHashSet<>();
        Collections.addAll(set, fixedProducts);
        return set;
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public String toString() {
        return "IRPactInputData{" +
                "scalarData=" + scalarData +
                ", agentGroups=" + Arrays.toString(agentGroups) +
                ", timeModels=" + Arrays.toString(timeModels) +
                ", productGroups=" + Arrays.toString(productGroups) +
                ", fixedProducts=" + Arrays.toString(fixedProducts) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRPactInputData inputData = (IRPactInputData) o;
        return Objects.equals(scalarData, inputData.scalarData) &&
                Arrays.equals(agentGroups, inputData.agentGroups) &&
                Arrays.equals(timeModels, inputData.timeModels) &&
                Arrays.equals(productGroups, inputData.productGroups) &&
                Arrays.equals(fixedProducts, inputData.fixedProducts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(scalarData);
        result = 31 * result + Arrays.hashCode(agentGroups);
        result = 31 * result + Arrays.hashCode(timeModels);
        result = 31 * result + Arrays.hashCode(productGroups);
        result = 31 * result + Arrays.hashCode(fixedProducts);
        return result;
    }

}
