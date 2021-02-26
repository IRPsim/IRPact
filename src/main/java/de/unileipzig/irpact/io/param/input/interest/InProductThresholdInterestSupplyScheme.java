package de.unileipzig.irpact.io.param.input.interest;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
@Todo("res testen")
public class InProductThresholdInterestSupplyScheme implements InProductInterestSupplyScheme {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductThresholdInterestSupplyScheme.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Awareness"),
                res.getCachedElement("Threshold")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Grenzwert")
                .setGamsDescription("Grenzwert ab dem das Produkt interessant wird")
                .store(InProductThresholdInterestSupplyScheme.class, "awarenessDistribution");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] awarenessDistribution;

    public InProductThresholdInterestSupplyScheme() {
    }

    public InProductThresholdInterestSupplyScheme(String name, InUnivariateDoubleDistribution awarenessDistribution) {
        this._name = name;
        setAwarenessDistribution(awarenessDistribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setAwarenessDistribution(InUnivariateDoubleDistribution awarenessDistribution) {
        this.awarenessDistribution = new InUnivariateDoubleDistribution[]{awarenessDistribution};
    }

    public InUnivariateDoubleDistribution getAwarenessDistribution() throws ParsingException {
        return InUtil.getInstance(awarenessDistribution, "AwarenessDistribution");
    }

    @Override
    public ProductThresholdInterestSupplyScheme parse(InputParser parser) throws ParsingException {
        ProductThresholdInterestSupplyScheme awa = new ProductThresholdInterestSupplyScheme();
        awa.setName(getName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getAwarenessDistribution());
        awa.setDistribution(dist);

        return awa;
    }
}
