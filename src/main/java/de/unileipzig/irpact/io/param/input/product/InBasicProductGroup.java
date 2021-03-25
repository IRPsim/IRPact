package de.unileipzig.irpact.io.param.input.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InBasicProductGroup implements InProductGroup {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Produkte"),
                res.getCachedElement("Gruppen_Product")
        );

        res.putPath(
                thisClass(), "pgAttributes",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Produkt-Attribut-Mapping")
        );
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InProductGroupAttribute[] pgAttributes;

    public InBasicProductGroup() {
    }

    public InBasicProductGroup(String name, InProductGroupAttribute[] attributes) {
        this._name = name;
        this.pgAttributes = attributes;
    }

    public String getName() {
        return _name;
    }

    public InProductGroupAttribute[] getAttributes() throws ParsingException {
        return ParamUtil.getNonNullArray(pgAttributes, "pgAttributes");
    }

    @Override
    public InProductGroupAttribute findAttribute(String name) throws ParsingException {
        for(InProductGroupAttribute attr: getAttributes()) {
            if(Objects.equals(name, attr.getAttributeName())) {
                return attr;
            }
        }
        throw new ParsingException("not found: " + name);
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
            if(bpg.hasGroupAttribute(attr.getName())) {
                throw new ParsingException("ProductGroupAttribute '" + attr.getName() + "' already exists in " + bpg.getName());
            }

            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ProductGroupAttribute '{}' ('{}') to group '{}'", attr.getName(), inAttr.getName(), bpg.getName());
            bpg.addGroupAttribute(attr);
        }

        return bpg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InBasicProductGroup)) return false;
        InBasicProductGroup that = (InBasicProductGroup) o;
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
