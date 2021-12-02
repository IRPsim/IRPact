package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_FIX;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_FIX)
public class InFixProduct implements InIRPactEntity {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InProductGroup[] refPG = new InProductGroup[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InFixProductAttribute[] fixPAttrs = new InFixProductAttribute[0];

    public InFixProduct() {
    }

    public InFixProduct(String name, InProductGroup grp, InFixProductAttribute[] attrs) {
        this.name = name;
        setProductGroup(grp);
        this.fixPAttrs = attrs;
    }

    @Override
    public InFixProduct copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixProduct newCopy(CopyCache cache) {
        InFixProduct copy = new InFixProduct();
        copy.name = name;
        copy.refPG = cache.copyArray(refPG);
        copy.fixPAttrs = cache.copyArray(fixPAttrs);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public BasicProduct parse(IRPactInputParser parser) throws ParsingException {
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
