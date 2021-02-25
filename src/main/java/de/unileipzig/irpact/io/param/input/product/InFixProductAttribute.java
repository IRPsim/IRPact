package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductAttribute implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFixProductAttribute.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Initiale_Produktattribute")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Gruppenattribut")
                .setGamsDescription("Gruppenattribut")
                .store(InFixProductAttribute.class, "refPGA");

        res.newEntryBuilder()
                .setGamsIdentifier("Fixierte Wert")
                .setGamsDescription("Wert")
                .store(InFixProductAttribute.class, "fixPAvalue");
    }

    public String _name;

    @FieldDefinition
    public InProductGroupAttribute refPGA;

    @FieldDefinition
    public double fixPAvalue;

    public InFixProductAttribute() {
    }

    public InFixProductAttribute(String name, InProductGroupAttribute grpAttr, double value) {
        this._name = name;
        this.refPGA = grpAttr;
        this.fixPAvalue = value;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InProductGroupAttribute getRefPGA() {
        return refPGA;
    }

    public double getValue() {
        return fixPAvalue;
    }

    @Override
    public ProductAttribute parse(InputParser parser) throws ParsingException {
        ProductGroupAttribute pgAttr = parser.parseEntityTo(getRefPGA());
        return pgAttr.derive(getName(), getValue());
    }
}
