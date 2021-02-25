package de.unileipzig.irpact.io.param.input.awareness;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.awareness.ProductThresholdAwarenessSupplyScheme;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
@Todo("res testen")
public class InProductThresholdAwarenessSupplyScheme implements InProductAwarenessSupplyScheme {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductThresholdAwarenessSupplyScheme.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Awareness"),
                res.getCachedElement("Threshold")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Grenzwert")
                .setGamsDescription("Grenzwert ab dem das Produkt interessant wird")
                .store(InProductThresholdAwarenessSupplyScheme.class, "awarenessDistribution");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution awarenessDistribution;

    public InProductThresholdAwarenessSupplyScheme() {
    }

    public InProductThresholdAwarenessSupplyScheme(String name, InUnivariateDoubleDistribution awarenessDistribution) {
        this._name = name;
        this.awarenessDistribution = awarenessDistribution;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InUnivariateDoubleDistribution getAwarenessDistribution() {
        return awarenessDistribution;
    }

    @Override
    public ProductThresholdAwarenessSupplyScheme parse(InputParser parser) throws ParsingException {
        ProductThresholdAwarenessSupplyScheme awa = new ProductThresholdAwarenessSupplyScheme();
        awa.setName(getName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getAwarenessDistribution());
        awa.setDistribution(dist);

        return awa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductThresholdAwarenessSupplyScheme)) return false;
        InProductThresholdAwarenessSupplyScheme that = (InProductThresholdAwarenessSupplyScheme) o;
        return Objects.equals(_name, that._name) && Objects.equals(awarenessDistribution, that.awarenessDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, awarenessDistribution);
    }
}
