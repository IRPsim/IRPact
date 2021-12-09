package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.attribute.BasicProductDoubleGroupAttribute;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_ATTR_BASIC;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_ATTR_BASIC)
public class InBasicProductGroupAttribute implements InDependentProductGroupAttribute {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] attrName = new InAttributeName[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] dist = new InUnivariateDoubleDistribution[0];

    public InBasicProductGroupAttribute() {
    }

    public InBasicProductGroupAttribute(
            String name,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this.name = name;
        setAttributeNameInstance(attributeName);
        setDistribution(distribution);
    }

    @Override
    public InBasicProductGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBasicProductGroupAttribute newCopy(CopyCache cache) {
        InBasicProductGroupAttribute copy = new InBasicProductGroupAttribute();
        copy.name = name;
        copy.attrName = cache.copyArray(attrName);
        copy.dist = cache.copyArray(dist);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setAttributeNameInstance(InAttributeName attrName) {
        this.attrName = new InAttributeName[]{attrName};
    }

    public InAttributeName getAttributeNameInstance() throws ParsingException {
        return ParamUtil.getInstance(attrName, "AttributeName");
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return getAttributeNameInstance().getName();
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicProductDoubleGroupAttribute parse(IRPactInputParser parser) throws ParsingException {
        BasicProductDoubleGroupAttribute pgAttr = new BasicProductDoubleGroupAttribute();
        pgAttr.setName(getAttributeName());
        pgAttr.setArtificial(false);

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        pgAttr.setDistribution(dist);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created ProductGroupAttribute '{}' ('{}')", pgAttr.getName(), getName());
        return pgAttr;
    }
}
