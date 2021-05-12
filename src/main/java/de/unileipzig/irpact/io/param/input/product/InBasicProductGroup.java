package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.attribute.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InBasicProductGroup implements InProductGroup {

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
        putClassPath(res, thisClass(), PRODUCTS, PRODUCTS_GROUP, thisName());
        addEntry(res, thisClass(), "pgAttributes");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InDependentProductGroupAttribute[] pgAttributes;

    public InBasicProductGroup() {
    }

    public InBasicProductGroup(String name, InDependentProductGroupAttribute[] attributes) {
        this._name = name;
        this.pgAttributes = attributes;
    }

    @Override
    public InBasicProductGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBasicProductGroup newCopy(CopyCache cache) {
        InBasicProductGroup copy = new InBasicProductGroup();
        copy._name = _name;
        copy.pgAttributes = cache.copyArray(pgAttributes);
        return copy;
    }

    public String getName() {
        return _name;
    }

    public InDependentProductGroupAttribute[] getAttributes() throws ParsingException {
        return ParamUtil.getNonNullArray(pgAttributes, "pgAttributes");
    }

    @Override
    public BasicProductGroup parse(InputParser parser) throws ParsingException {
        BasicProductManager productManager = (BasicProductManager) parser.getEnvironment().getProducts();
        BasicProductGroup bpg = new BasicProductGroup();
        bpg.setEnvironment(parser.getEnvironment());
        bpg.setName(getName());
        if(productManager.has(bpg.getName())) {
            throw new ParsingException("ProductGroup '" + bpg.getName() + "' already exists");
        }

        for(InDependentProductGroupAttribute inAttr: getAttributes()) {
            ProductGroupAttribute attr = parser.parseEntityTo(inAttr);
            if(bpg.hasGroupAttribute(attr.getName())) {
                throw new ParsingException("ProductGroupAttribute '" + attr.getName() + "' already exists in " + bpg.getName());
            }

            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add ProductGroupAttribute '{}' ('{}') to group '{}'", attr.getName(), inAttr.getName(), bpg.getName());
            bpg.addGroupAttribute(attr);
        }

        return bpg;
    }
}
