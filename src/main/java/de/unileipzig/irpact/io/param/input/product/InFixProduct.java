package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProduct implements InEntity {

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
        addEntry(res, thisClass(), "refPG");
        addEntry(res, thisClass(), "fixPAttrs");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InProductGroup[] refPG;

    @FieldDefinition
    public InFixProductAttribute[] fixPAttrs;

    public InFixProduct() {
    }

    public InFixProduct(String name, InProductGroup grp, InFixProductAttribute[] attrs) {
        this._name = name;
        setProductGroup(grp);
        this.fixPAttrs = attrs;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setProductGroup(InProductGroup refPG) {
        this.refPG = new InProductGroup[]{refPG};
    }

    public InProductGroup getProductGroup() throws ParsingException {
        return ParamUtil.getInstance(refPG, "ProductGroup");
    }

    public InFixProductAttribute[] getAttributes() {
        return fixPAttrs;
    }

    public void setAttributes(Collection<? extends InFixProductAttribute> attrs) {
        fixPAttrs = attrs.toArray(new InFixProductAttribute[0]);
    }

    @Override
    public BasicProduct parse(InputParser parser) throws ParsingException {
        BasicProduct bp = new BasicProduct();
        bp.setEnvironment(parser.getEnvironment());
        bp.setFixed(true);
        bp.setName(getName());

        BasicProductGroup bpg = parser.parseEntityTo(getProductGroup());
        bp.setGroup(bpg);
        if(bpg.hasProduct(bp.getName())) {
            throw new ParsingException("Product '" + bp.getName() + "' already exists in group '" + bpg.getName() + "'");
        }

        for(InFixProductAttribute inAttr: getAttributes()) {
            ProductAttribute pAttr = parser.parseEntityTo(inAttr);
            if(bp.hasAttribute(pAttr.getName())) {
                throw new ParsingException("Attribute '" + pAttr.getName() + "' already exists in product '" + bp.getName() + "'");
            }
            bp.addAttribute(pAttr);
        }

        return bp;
    }
}
