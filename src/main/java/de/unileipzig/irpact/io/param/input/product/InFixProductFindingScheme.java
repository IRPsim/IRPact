package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductFindingScheme implements InProductFindingScheme {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFixProductFindingScheme.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("InProductFindingScheme"),
                res.getCachedElement("InFixProductFindingScheme")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Produkt")
                .setGamsDescription("Produkt")
                .store(InFixProductFindingScheme.class, "refFixProduct");
    }

    public String _name;

    @FieldDefinition
    public InFixProduct refFixProduct;

    public InFixProductFindingScheme() {
    }

    public InFixProductFindingScheme(String name, InFixProduct product) {
        this._name = name;
        this.refFixProduct = product;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InFixProduct getFixProduct() {
        return refFixProduct;
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
