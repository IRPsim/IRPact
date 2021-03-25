package de.unileipzig.irpact.io.spec2.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FixProductSpec extends AbstractSubSpec<InFixProduct> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixProductSpec.class);

    public static final FixProductSpec INSTANCE = new FixProductSpec();
    public static final String TYPE = "FixProduct";

    @Override
    public boolean isType(String type) {
        return Objects.equals(type, TYPE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isInstance(Object input) {
        return input instanceof InFixProduct;
    }

    @Override
    protected InFixProduct[] newArray(int len) {
        return new InFixProduct[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFixProduct[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InFixProduct toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InFixProduct product = new InFixProduct();
        product.setName(name);
        InProductGroup pg = job.findProductGroup(rootSpec.getText(TAG_group));
        product.setProductGroup(pg);

        List<InFixProductAttribute> attrList = new ArrayList<>();
        SpecificationHelper2 attrsSpec = rootSpec.getArray(TAG_attributes);
        for(SpecificationHelper2 attrSpec: attrsSpec.iterateElements()) {
            InProductGroupAttribute grpAttr = pg.findAttribute(attrSpec.getText(TAG_group));
            double value = attrSpec.getDouble(TAG_value);
            InFixProductAttribute attr = new InFixProductAttribute();
            attr.setName(name, grpAttr.getAttributeName());
            attr.setGroupAttribute(grpAttr);
            attr.setValue(value);
            attrList.add(attr);
        }
        product.setAttributes(attrList);

        job.cache(name, product);
        return product;
    }

    @Override
    public Class<InFixProduct> getParamType() {
        return InFixProduct.class;
    }

    @Override
    public void toSpec(InFixProduct input, SpecificationJob2 job) throws ParsingException {
        String pgName = input.getProductGroup().getName();
        SpecificationHelper2 pgSpec = toHelper(job.getData().getProductGroups().get(pgName));
        SpecificationHelper2 fpSpec = pgSpec.getArray(TAG_fixProducts);
        create(input, fpSpec, job);
    }

    @Override
    protected void create(InFixProduct input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_group, input.getProductGroup().getName());

        SpecificationHelper2 attrsSpec = rootSpec.getOrCreateArray(TAG_attributes);
        for(InFixProductAttribute attr: input.getAttributes()) {
            SpecificationHelper2 attrSpec = attrsSpec.addObject();
            attrSpec.set(TAG_group, attr.getGroupAttribute().getAttributeName());
            attrSpec.set(TAG_value, attr.getValue());
        }
    }
}
