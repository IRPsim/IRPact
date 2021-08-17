package de.unileipzig.irpact.io.spec.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
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
    public InFixProduct[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InFixProduct toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        if(true)
            throw new TodoException();

        InFixProduct product = new InFixProduct();
        product.setName(name);
        InProductGroup pg = job.findProductGroup(rootSpec.getText(TAG_group));
        product.setProductGroup(pg);

        List<InFixProductAttribute> attrList = new ArrayList<>();
        SpecificationHelper attrsSpec = rootSpec.getArray(TAG_attributes);
        for(SpecificationHelper attrSpec: attrsSpec.iterateElements()) {
//            InProductGroupAttribute grpAttr = pg.findAttribute(attrSpec.getText(TAG_group)); //TODO
//            double value = attrSpec.getDouble(TAG_value);
//            InFixProductAttribute attr = new InFixProductAttribute();
//            attr.setName(name, grpAttr.getAttributeName());
//            attr.setGroupAttribute(grpAttr);
//            attr.setValue(value);
//            attrList.add(attr);
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
    public void toSpec(InFixProduct input, SpecificationJob job) throws ParsingException {
        String pgName = input.getProductGroup().getName();
        SpecificationHelper pgSpec = toHelper(job.getData().getProductGroups().get(pgName));
        SpecificationHelper fpSpec = pgSpec.getArray(TAG_fixProducts);
        create(input, fpSpec, job);
    }

    @Override
    protected void create(InFixProduct input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_group, input.getProductGroup().getName());

        SpecificationHelper attrsSpec = rootSpec.getOrCreateArray(TAG_attributes);
        for(InFixProductAttribute attr: input.getAttributes()) {
            SpecificationHelper attrSpec = attrsSpec.addObject();
            attrSpec.set(TAG_group, attr.getGroupAttribute().getAttributeName());
            attrSpec.set(TAG_value, attr.getValue());
        }
    }
}
