package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;

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
        TreeResourceApplier.callAllSubInitResSilently(thisClass(), res);
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Produkte"),
                res.getCachedElement("Initiale_Produktattribute")
        );
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
    }
    public static void applyRes0(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Gruppenattribut")
                .setGamsDescription("Gruppenattribut")
                .store(thisClass(), "refPGA");
    }
    @FieldDefinition
    public InProductGroupAttribute[] refPGA;

    public static void initRes1(TreeAnnotationResource res) {
    }
    public static void applyRes1(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Fixierte Wert")
                .setGamsDescription("Wert")
                .store(thisClass(), "fixPAvalue");
    }
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
        ProductGroupAttribute pgAttr = parser.parseEntityTo(getGroupAttribute());
        return pgAttr.derive(getName(), getValue());
    }
}
