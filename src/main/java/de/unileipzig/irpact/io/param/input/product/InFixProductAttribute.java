package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.core.product.attribute.ProductDoubleGroupAttribute;
import de.unileipzig.irpact.core.product.attribute.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductAttribute implements InEntity {

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
        putClassPath(res, thisClass(), PRODUCTS, thisName());
        addEntry(res, thisClass(), "refPGA");
        addEntry(res, thisClass(), "fixPAvalue");
    }

    public String _name;

    @FieldDefinition
    public InProductGroupAttribute[] refPGA;

    @FieldDefinition
    public double fixPAvalue;

    public InFixProductAttribute() {
    }

    public InFixProductAttribute(String name, InProductGroupAttribute grpAttr, double value) {
        this._name = name;
        setGroupAttribute(grpAttr);
        this.fixPAvalue = value;
    }

    @Override
    public InFixProductAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixProductAttribute newCopy(CopyCache cache) {
        InFixProductAttribute copy = new InFixProductAttribute();
        copy._name = _name;
        copy.refPGA = cache.copyArray(refPGA);
        copy.fixPAvalue = fixPAvalue;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String fullName) {
        this._name = fullName;
    }

    public void setName(String grpName, String attributeName) {
        this._name = ParamUtil.concName(grpName, attributeName);
    }

    public String getAttributeName() throws ParsingException {
        return getGroupAttribute().getAttributeName();
    }

    public void setGroupAttribute(InProductGroupAttribute refPGA) {
        this.refPGA = new InProductGroupAttribute[]{refPGA};
    }

    public InProductGroupAttribute getGroupAttribute() throws ParsingException {
        return ParamUtil.getInstance(refPGA, "ProductGroupAttribute");
    }

    public double getValue() {
        return fixPAvalue;
    }

    public void setValue(double fixPAvalue) {
        this.fixPAvalue = fixPAvalue;
    }

    @Override
    public ProductAttribute parse(InputParser parser) throws ParsingException {
        ProductDoubleGroupAttribute pgAttr = parser.parseEntityTo(getGroupAttribute());
        return pgAttr.derive(getName(), getValue());
    }
}
