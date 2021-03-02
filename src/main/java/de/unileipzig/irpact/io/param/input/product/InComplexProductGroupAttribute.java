package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public class InComplexProductGroupAttribute implements I_InProductGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InComplexProductGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InProductGroup[] pg;

    @FieldDefinition
    public InAttributeName[] attrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dist;

    public InComplexProductGroupAttribute() {
    }

    public InComplexProductGroupAttribute(
            String name,
            InProductGroup pg,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this._name = name;
        setProductGroup(pg);
        setAttributeNameInstance(attributeName);
        setDistribution(distribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setAttributeNameInstance(InAttributeName attrName) {
        this.attrName = new InAttributeName[]{attrName};
    }

    public InAttributeName getAttributeNameInstance() throws ParsingException {
        return ParamUtil.getInstance(attrName, "AttributeName");
    }

    public String getAttributeName() throws ParsingException {
        return getAttributeNameInstance().getName();
    }

    public void setProductGroup(InProductGroup pg) {
        this.pg = new InProductGroup[]{pg};
    }

    public InProductGroup getProductGroup() throws ParsingException {
        return ParamUtil.getInstance(pg, "ProductGroup");
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicProductGroupAttribute parse(InputParser parser) throws ParsingException {
        InProductGroup inPg = getProductGroup();
        BasicProductGroup pg = parser.parseEntityTo(inPg);

        BasicProductGroupAttribute pgAttr = new BasicProductGroupAttribute();
        pgAttr.setName(getAttributeName());

        if(pg.hasGroupAttribute(pgAttr)) {
            throw new ParsingException("ProductGroupAttribute '" + pgAttr.getName() + "' already exists in '" + pg.getName() + "'");
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        pgAttr.setDistribution(dist);

        pg.addGroupAttribute(pgAttr);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "added ProductGroupAttribute '{}' ('{}') to group '{}'", pgAttr.getName(), getName(), pg.getName());

        return pgAttr;
    }
}
