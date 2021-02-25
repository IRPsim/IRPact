package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProduct implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFixProduct.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Initiale_Produkte")
        );

        res.putPath(
                InFixProduct.class, "fixPAttrs",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Initiale_Produkte"),
                res.getCachedElement("Initiale_Produkt-Attribut-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Produktgruppe")
                .setGamsDescription("Zugehörige Gruppe")
                .store(InFixProduct.class, "refPG");

        res.newEntryBuilder()
                .setGamsIdentifier("Attribute")
                .setGamsDescription("Attribute")
                .store(InFixProduct.class, "fixPAttrs");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InFixProduct.class);

    public String _name;

    @FieldDefinition
    public InProductGroup refPG;

    @FieldDefinition
    public InFixProductAttribute[] fixPAttrs;

    public InFixProduct() {
    }

    public InFixProduct(String name, InProductGroup grp, InFixProductAttribute[] attrs) {
        this._name = name;
        this.refPG = grp;
        this.fixPAttrs = attrs;
    }

    public void setAttributes(Collection<? extends InFixProductAttribute> attrs) {
        fixPAttrs = attrs.toArray(new InFixProductAttribute[0]);
    }

    @Override
    public String getName() {
        return _name;
    }

    public InProductGroup getProductGroup() {
        return refPG;
    }

    public InFixProductAttribute[] getAttributes() {
        return fixPAttrs;
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
