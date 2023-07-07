package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.core.product.attribute.ProductDoubleGroupAttribute;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructureEnum;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_FIXATTR;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_FIXATTR)
public class InFixProductAttribute implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double fixPAvalue = 0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InProductGroupAttribute[] refPGA = new InProductGroupAttribute[0];

    public InFixProductAttribute() {
    }

    public InFixProductAttribute(String name, InProductGroupAttribute grpAttr, double value) {
        this.name = name;
        setGroupAttribute(grpAttr);
        this.fixPAvalue = value;
    }

    @Override
    public InFixProductAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixProductAttribute newCopy(CopyCache cache) {
        InFixProductAttribute copy = new InFixProductAttribute();
        copy.name = name;
        copy.refPGA = cache.copyArray(refPGA);
        copy.fixPAvalue = fixPAvalue;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public void setName(String grpName, String attributeName) {
        this.name = ParamUtil.concName(grpName, attributeName);
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
    public ProductAttribute parse(IRPactInputParser parser) throws ParsingException {
        ProductDoubleGroupAttribute pgAttr = parser.parseEntityTo(getGroupAttribute());
        return pgAttr.derive(getName(), getValue());
    }
}
