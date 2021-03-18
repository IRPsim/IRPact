package de.unileipzig.irpact.io.param.input.interest;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
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
    public InUnivariateDoubleDistribution[] interestDistribution;

    public InProductThresholdInterestSupplyScheme() {
    }

    public InProductThresholdInterestSupplyScheme(String name, InUnivariateDoubleDistribution interestDistribution) {
        this._name = name;
        setInterestDistribution(interestDistribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setInterestDistribution(InUnivariateDoubleDistribution interestDistribution) {
        this.interestDistribution = new InUnivariateDoubleDistribution[]{interestDistribution};
    }

    public InUnivariateDoubleDistribution getInterestDistribution() throws ParsingException {
        return ParamUtil.getInstance(interestDistribution, "interestDistribution");
    }

    @Override
    public ProductThresholdInterestSupplyScheme parse(InputParser parser) throws ParsingException {
        ProductThresholdInterestSupplyScheme awa = new ProductThresholdInterestSupplyScheme();
        awa.setName(getName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getInterestDistribution());
        awa.setDistribution(dist);

        return awa;
    }
}
