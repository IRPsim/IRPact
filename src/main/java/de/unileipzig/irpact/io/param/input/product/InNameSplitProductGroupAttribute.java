package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.attribute.BasicProductDoubleGroupAttribute;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS;
import static de.unileipzig.irpact.io.param.IOConstants.PRODUCTS_ATTR;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_ATTR_SPLIT;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_ATTR_SPLIT)
public class InNameSplitProductGroupAttribute implements InIndependentProductGroupAttribute {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InNameSplitProductGroupAttribute.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] dist = new InUnivariateDoubleDistribution[0];

    public InNameSplitProductGroupAttribute() {
    }

    @Override
    public InNameSplitProductGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNameSplitProductGroupAttribute newCopy(CopyCache cache) {
        InNameSplitProductGroupAttribute copy = new InNameSplitProductGroupAttribute();
        copy.name = name;
        copy.dist = cache.copyArray(dist);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProductGroupName() throws ParsingException {
        return ParamUtil.firstPart(getName());
    }

    @Override
    public InProductGroup getProductGroup(InputParser p) throws ParsingException {
        IRPactInputParser parser = (IRPactInputParser) p;
        return parser.getRoot().getProductGroup(getProductGroupName());
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return ParamUtil.secondPart(getName());
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    @Override
    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        InProductGroup inPg = getProductGroup(parser);
        BasicProductGroup pg = parser.parseEntityTo(inPg);
        if(parser.isRestored() && pg.hasGroupAttribute(getAttributeName())) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "skip ProductGroupAttribute '{}' in '{}'", getAttributeName(), pg.getName());
            return;
        }

        BasicProductDoubleGroupAttribute pgAttr = new BasicProductDoubleGroupAttribute();
        pgAttr.setName(getAttributeName());
        pgAttr.setArtificial(false);

        if(pg.hasGroupAttribute(pgAttr)) {
            throw new ParsingException("ProductGroupAttribute '" + pgAttr.getName() + "' already exists in '" + pg.getName() + "'");
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        pgAttr.setDistribution(dist);

        pg.addGroupAttribute(pgAttr);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added ProductGroupAttribute '{}' ('{}') to group '{}'", pgAttr.getName(), getName(), pg.getName());
    }
}
