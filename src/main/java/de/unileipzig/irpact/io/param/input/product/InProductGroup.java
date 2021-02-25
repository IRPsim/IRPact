package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductGroup implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductGroup.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Gruppen_Product")
        );

        res.putPath(
                InProductGroup.class, "pgAttributes",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Produkt-Attribut-Mapping")
        );
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InProductGroup.class);

    public String _name;

    @FieldDefinition
    public InProductGroupAttribute[] pgAttributes;

    public InProductGroup() {
    }

    public InProductGroup(String name, InProductGroupAttribute[] attributes) {
        this._name = name;
        this.pgAttributes = attributes;
    }

    public String getName() {
        return _name;
    }

    public InProductGroupAttribute[] getAttributes() {
        return pgAttributes;
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

        for(InProductGroupAttribute inAttr: getAttributes()) {
            ProductGroupAttribute attr = parser.parseEntityTo(inAttr);
            if(bpg.hasAttribute(attr.getName())) {
                throw new ParsingException("ProductGroupAttribute '" + attr.getName() + "' already exists in " + bpg.getName());
            }

            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ProductGroupAttribute '{}' ('{}') to group '{}'", attr.getName(), inAttr.getName(), bpg.getName());
            bpg.addAttribute(attr);
        }

        return bpg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductGroup)) return false;
        InProductGroup that = (InProductGroup) o;
        return Objects.equals(_name, that._name) && Arrays.equals(pgAttributes, that.pgAttributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name);
        result = 31 * result + Arrays.hashCode(pgAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InProductGroup{" +
                "_name='" + _name + '\'' +
                ", pgAttributes=" + Arrays.toString(pgAttributes) +
                '}';
    }
}
