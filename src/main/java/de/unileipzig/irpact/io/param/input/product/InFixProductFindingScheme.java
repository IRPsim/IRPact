package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_FINDSCHE_FIX;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_FINDSCHE_FIX)
public class InFixProductFindingScheme implements InProductFindingScheme {

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
    public InFixProduct[] refFixProduct = new InFixProduct[0];

    public InFixProductFindingScheme() {
    }

    public InFixProductFindingScheme(String name, InFixProduct product) {
        this.name = name;
        setFixProduct(product);
    }

    @Override
    public InFixProductFindingScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixProductFindingScheme newCopy(CopyCache cache) {
        InFixProductFindingScheme copy = new InFixProductFindingScheme();
        copy.name = name;
        copy.refFixProduct = cache.copyArray(refFixProduct);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
