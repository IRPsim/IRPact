package de.unileipzig.irpact.misc;

import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.param.input.network.InCompleteGraphTopology;
import de.unileipzig.irpact.io.param.input.network.InDistanceEvaluator;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.base.Config;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public final class ExampleUtil {

    public static final Map<String, InAttributeName> NAMES = new HashMap<>();

    public static final InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0.0);
    public static final InConstantUnivariateDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1.0);
    public static final InConstantUnivariateDistribution diraq9 = new InConstantUnivariateDistribution("diraq9", 9);
    public static final InConstantUnivariateDistribution diraq10 = new InConstantUnivariateDistribution("diraq10", 10.0);

    public static final InRandomBoundedIntegerDistribution rnd090 = new InRandomBoundedIntegerDistribution("rnd090", 0, 91);

    private static void addName(String name) {
        NAMES.put(name, new InAttributeName(name));
    }

    static {
        addName(RAConstants.PURCHASE_POWER);
        addName(RAConstants.NOVELTY_SEEKING);
        addName(RAConstants.NOVELTY_SEEKING_UNCERTAINTY);
//        addName(RAConstants.NOVELTY_SEEKING_CONVERGENCE);
        addName(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        addName(RAConstants.DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY);
//        addName(RAConstants.DEPENDENT_JUDGMENT_MAKING_CONVERGENCE);
        addName(RAConstants.ENVIRONMENTAL_CONCERN);
        addName(RAConstants.PURCHASE_POWER);
        addName(RAConstants.ENVIRONMENTAL_CONCERN_UNCERTAINTY);
//        addName(RAConstants.ENVIRONMENTAL_CONCERN_CONVERGENCE);
        addName(RAConstants.SHARE_1_2_HOUSE);
        addName(RAConstants.HOUSE_OWNER);
        addName(RAConstants.CONSTRUCTION_RATE);
        addName(RAConstants.RENOVATION_RATE);

        addName(RAConstants.COMMUNICATION_FREQUENCY_SN);

        addName(RAConstants.INITIAL_PRODUCT_AWARENESS);
        addName(RAConstants.INTEREST_THRESHOLD);
        addName(RAConstants.FINANCIAL_THRESHOLD);
        addName(RAConstants.ADOPTION_THRESHOLD);

        addName(RAConstants.INVESTMENT_COST);
    }

    public static InConsumerAgentGroupAttribute createAttribute(String cag, String attrName) {
        return new InConsumerAgentGroupAttribute(
                cag + "_" + attrName,
                NAMES.get(attrName),
                diraq0
        );
    }

    public static InConsumerAgentGroup buildRelativeAgreementCag(String name, int numberOfAgents) {
        InConsumerAgentGroup cag = new InConsumerAgentGroup();
        cag._name = name;
        cag.informationAuthority = 1.0;
        cag.numberOfAgentsX = numberOfAgents;
        cag.setAwareness(new InProductThresholdInterestSupplyScheme(name + "_tawa", diraq10));
        cag.cagAttributes = new InConsumerAgentGroupAttribute[]{
                createAttribute(name, RAConstants.PURCHASE_POWER),
                createAttribute(name, RAConstants.NOVELTY_SEEKING),
                createAttribute(name, RAConstants.NOVELTY_SEEKING_UNCERTAINTY),
//                createAttribute(name, RAConstants.NOVELTY_SEEKING_CONVERGENCE),
                createAttribute(name, RAConstants.DEPENDENT_JUDGMENT_MAKING),
                createAttribute(name, RAConstants.DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY),
//                createAttribute(name, RAConstants.DEPENDENT_JUDGMENT_MAKING_CONVERGENCE),
                createAttribute(name, RAConstants.ENVIRONMENTAL_CONCERN),
                createAttribute(name, RAConstants.ENVIRONMENTAL_CONCERN_UNCERTAINTY),
//                createAttribute(name, RAConstants.ENVIRONMENTAL_CONCERN_CONVERGENCE),
                createAttribute(name, RAConstants.SHARE_1_2_HOUSE),
                createAttribute(name, RAConstants.HOUSE_OWNER),
                createAttribute(name, RAConstants.CONSTRUCTION_RATE),
                createAttribute(name, RAConstants.RENOVATION_RATE),

                createAttribute(name, RAConstants.COMMUNICATION_FREQUENCY_SN),

                createAttribute(name, RAConstants.INITIAL_PRODUCT_AWARENESS),
                createAttribute(name, RAConstants.INTEREST_THRESHOLD),
                createAttribute(name, RAConstants.FINANCIAL_THRESHOLD),
                createAttribute(name, RAConstants.ADOPTION_THRESHOLD)
        };
        return cag;
    }

    public static InComplexAffinityEntry[] buildAffinityEntries(InConsumerAgentGroup... cags) {
        List<InComplexAffinityEntry> list = new ArrayList<>();
        for(InConsumerAgentGroup src: cags) {
            for(InConsumerAgentGroup tar: cags) {
                double value = src == tar ? 1.0 : 0.0;
                InComplexAffinityEntry entry = new InComplexAffinityEntry(src.getName() + "_" + tar.getName(), src, tar, value);
                list.add(entry);
            }
        }
        return list.toArray(new InComplexAffinityEntry[0]);
    }

    public static InGraphTopologyScheme[] getCompleteGraph() {
        InCompleteGraphTopology topo = new InCompleteGraphTopology("Complete_Graph", 1.0);
        return new InGraphTopologyScheme[]{topo};
    }

    public static InDistanceEvaluator[] getNoDistance() {
        return new InDistanceEvaluator[]{new InNoDistance("No_Distance")};
    }

    public static AnnualEntry<InRoot> buildEntry(InRoot root) {
        Config config = new Config(IRPactJson.JSON.createObjectNode());
        config.init();
        config.setYear(root.general.startYear);
        return new AnnualEntry<>(root, config.root());
    }

    public static InProcessModel[] buildRelativeAgreementModel() {
//        InRAProcessModel model = new InRAProcessModel("RA", 0.25, 0.25, 0.25, 0.25, 3, 2, 1, 0);
//        return new InProcessModel[]{model};
        throw new UnsupportedOperationException();
    }

    public static InProductGroup buildPV() {
        return new InProductGroup(
                "PV",
                new InProductGroupAttribute[]{
                        new InProductGroupAttribute(RAConstants.INVESTMENT_COST, NAMES.get(RAConstants.INVESTMENT_COST), diraq0)
                }
        );
    }

    public static InSpatialModel[] space2D() {
        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);
        return new InSpatialModel[]{space2D};
    }

    public static InSpatialDistribution[] buildSpatialDists(InConsumerAgentGroup... cags) {
        throw new TodoException();
//        return Arrays.stream(cags)
//                .map(cag -> new InCustomSpatialDistribution2D(
//                        cag + "_sdist",
//                        cag,
//                        0,
//                        0
//                ))
//                .toArray(InSpatialDistribution[]::new);
    }

    public static InTimeModel[] buildDiscreteMonth() {
        return new InTimeModel[]{
                new InDiscreteTimeModel("Diskret", TimeUnit.DAYS.toMillis(31))
        };
    }

    public static InOrientationSupplier[] getDefaultOrientation() {
        return new InOrientationSupplier[]{
                new InOrientationSupplier("O_Supp", rnd090)
        };
    }

    public static InSlopeSupplier[] getDefaultSlope() {
        return new InSlopeSupplier[]{
                new InSlopeSupplier("S_Supp", rnd090)
        };
    }

    public static InGeneral buildGeneral() {
        InGeneral general = new InGeneral();
        general.startYear = 2015;
        general.endYear = 2015;
        general.timeout = TimeUnit.MINUTES.toMillis(5);
        general.seed = 42;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logParamInit = true;
        general.logGraphCreation = true;
        general.logAgentCreation = true;
        general.logPlatformCreation = true;
        general.logTools = true;
        general.logJadexSystemOut = true;
        return general;
    }
}
