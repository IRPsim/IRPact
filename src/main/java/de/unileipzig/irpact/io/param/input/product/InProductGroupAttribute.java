package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductGroupAttribute implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductGroupAttribute.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Attribute_Product")
        );

        res.putPath(
                InProductGroupAttribute.class, "attrName",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Namen-Mapping_Product")
        );

        res.putPath(
                InProductGroupAttribute.class, "attrDistribution",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Verteilungs-Mapping_Product")
        );
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InProductGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InAttributeName attrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution attrDistribution;

    public InProductGroupAttribute() {
    }

    public InProductGroupAttribute(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrName = attributeName;
        this.attrDistribution = distribution;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InAttributeName getAttrName() {
        return attrName;
    }

    public InUnivariateDoubleDistribution getAttrDistribution() {
        return attrDistribution;
    }

    @Override
    public BasicProductGroupAttribute parse(InputParser parser) throws ParsingException {
        BasicProductGroupAttribute attr = new BasicProductGroupAttribute();
        attr.setName(getAttrName().getName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getAttrDistribution());
        attr.setDistribution(dist);

        return attr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductGroupAttribute)) return false;
        InProductGroupAttribute that = (InProductGroupAttribute) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrName, that.attrName) && Objects.equals(attrDistribution, that.attrDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrName, attrDistribution);
    }

    @Override
    public String toString() {
        return "InProductGroupAttribute{" +
                "_name='" + _name + '\'' +
                ", attrName=" + attrName +
                ", attrDistribution=" + attrDistribution +
                '}';
    }
}
