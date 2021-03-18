package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductFindingScheme implements InProductFindingScheme {

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
                res.getCachedElement("InProductFindingScheme"),
                res.getCachedElement("InFixProductFindingScheme")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Produkt")
                .setGamsDescription("Produkt")
                .store(thisClass(), "refFixProduct");
    }

    public String _name;

    @FieldDefinition
    public InFixProduct[] refFixProduct;

    public InFixProductFindingScheme() {
    }

    public InFixProductFindingScheme(String name, InFixProduct product) {
        this._name = name;
        setFixProduct(product);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setFixProduct(InFixProduct refFixProduct) {
        this.refFixProduct = new InFixProduct[]{refFixProduct};
    }

    public InFixProduct getFixProduct() throws ParsingException {
        return ParamUtil.getInstance(refFixProduct, "FixProduct");
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        FixProductFindingScheme scheme = new FixProductFindingScheme();
        scheme.setName(getName());

        Product product = parser.parseEntityTo(getFixProduct());
        scheme.setProduct(product);

        return scheme;
    }
}
