package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductAttribute implements InEntity {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Produkte"),
                res.getCachedElement("Initiale_Produktattribute")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Gruppenattribut")
                .setGamsDescription("Gruppenattribut")
                .store(thisClass(), "refPGA");

        res.newEntryBuilder()
                .setGamsIdentifier("Fixierte Wert")
                .setGamsDescription("Wert")
                .store(thisClass(), "fixPAvalue");
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
        setRefPGA(grpAttr);
        this.fixPAvalue = value;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setRefPGA(InProductGroupAttribute refPGA) {
        this.refPGA = new InProductGroupAttribute[]{refPGA};
    }

    public InProductGroupAttribute getRefPGA() throws ParsingException {
        return InUtil.getInstance(refPGA, "ProductGroupAttribute");
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
