package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.attribute.ProductGroupAttribute;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_GROUP_BASIC;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_GROUP_BASIC)
public class InBasicProductGroup implements InProductGroup {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InDependentProductGroupAttribute[] pgAttributes = new InDependentProductGroupAttribute[0];

    public InBasicProductGroup() {
    }

    public InBasicProductGroup(String name, InDependentProductGroupAttribute[] attributes) {
        this.name = name;
        this.pgAttributes = attributes;
    }

    @Override
    public InBasicProductGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBasicProductGroup newCopy(CopyCache cache) {
        InBasicProductGroup copy = new InBasicProductGroup();
        copy.name = name;
        copy.pgAttributes = cache.copyArray(pgAttributes);
        return copy;
    }

    public String getName() {
        return name;
    }

    public InDependentProductGroupAttribute[] getAttributes() throws ParsingException {
        return ParamUtil.getNonNullArray(pgAttributes, "pgAttributes");
    }

    @Override
    public BasicProductGroup parse(IRPactInputParser parser) throws ParsingException {
        BasicProductManager productManager = (BasicProductManager) parser.getEnvironment().getProducts();

        if(parser.isRestored() && productManager.has(getName())) {
            BasicProductGroup bpg = (BasicProductGroup) productManager.getGroup(getName());
            return update(parser, bpg);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse ProductGroup '{}'", getName());

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

    public BasicProductGroup update(IRPactInputParser parser, BasicProductGroup restored) throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "update ProductGroup '{}'", getName());

        for(InDependentProductGroupAttribute inAttr: getAttributes()) {
            if(restored.hasGroupAttribute(inAttr.getAttributeName())) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "skip existing ProductGroupAttribute '{}' in '{}'", inAttr.getAttributeName(), getName());
                continue;
            }

            ProductGroupAttribute attr = parser.parseEntityTo(inAttr);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add ProductGroupAttribute '{}' ('{}') to group '{}'", attr.getName(), inAttr.getName(), restored.getName());
            restored.addGroupAttribute(attr);
        }

        return restored;
    }
}
