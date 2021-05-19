package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS;
import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS_FINDING_SCHEME;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixProductFindingScheme implements InProductFindingScheme {

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
        putClassPath(res, thisClass(), PRODUCTS, PRODUCTS_FINDING_SCHEME, thisName());
        addEntry(res, thisClass(), "refFixProduct");
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
    public InFixProductFindingScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixProductFindingScheme newCopy(CopyCache cache) {
        InFixProductFindingScheme copy = new InFixProductFindingScheme();
        copy._name = _name;
        copy.refFixProduct = cache.copyArray(refFixProduct);
        return copy;
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
    public Object parse(IRPactInputParser parser) throws ParsingException {
        FixProductFindingScheme scheme = new FixProductFindingScheme();
        scheme.setName(getName());

        Product product = parser.parseEntityTo(getFixProduct());
        scheme.setProduct(product);

        return scheme;
    }
}
