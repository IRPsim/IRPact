package de.unileipzig.irpact.io;

import de.unileipzig.irpact.core.misc.DebugLevel;
import de.unileipzig.irpact.core.misc.StandardNames;
import de.unileipzig.irpact.io.input.IGeneralSettings;
import de.unileipzig.irpact.io.input.IRoot;
import de.unileipzig.irpact.io.input.affinity.IBasicAffinitiesEntry;
import de.unileipzig.irpact.io.input.affinity.IBasicAffinityMapping;
import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.IFixedProductAwareness;
import de.unileipzig.irpact.io.input.awareness.IThresholdAwareness;
import de.unileipzig.irpact.io.input.distribution.IBooleanDistribution;
import de.unileipzig.irpact.io.input.distribution.IRandomBoundedDistribution;
import de.unileipzig.irpact.io.input.network.IFastHeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.io.input.network.IFastHeterogeneousSmallWorldTopologyEntry;
import de.unileipzig.irpact.io.input.product.IFixedProduct;
import de.unileipzig.irpact.io.input.product.IFixedProductAttribute;
import de.unileipzig.irpact.io.input.product.IProductGroup;
import de.unileipzig.irpact.io.input.product.IProductGroupAttribute;
import de.unileipzig.irpact.io.input.spatial.ISpace2D;
import de.unileipzig.irpact.io.input.time.IDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.ITimeModel;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;

/**
 * @author Daniel Abitz
 */
public class DefaultScenario implements DefaultScenarioFactory {

    public static final DefaultScenario INSTANCE = new DefaultScenario();

    public DefaultScenario() {
    }

    protected IConsumerAgentGroupAttribute createAttribute(int id, String name, long seed) {
        return new IConsumerAgentGroupAttribute(
                name + "_" + id,
                new IRandomBoundedDistribution(name + "_dist_" + id, 0, 1000, seed)
        );
    }

    protected IConsumerAgentGroupAttribute createBooleanAttribute(int id, String name, long seed) {
        return new IConsumerAgentGroupAttribute(
                name + "_" + id,
                new IBooleanDistribution(name + "_dist_" + id, seed)
        );
    }

    protected IConsumerAgentGroup createGroup(int id, String name, int numberOfAgents, double informationAuthority) {
        IConsumerAgentGroup grp = new IConsumerAgentGroup();
        grp._name = name;
        grp.numberOfAgents = numberOfAgents;
        grp.productAwareness = new IThresholdAwareness(name + "_awa_" + id, 10.0);
        grp.informationAuthority = informationAuthority;
        grp.cagAttributes = new IConsumerAgentGroupAttribute[]{
                createAttribute(id, StandardNames.PURCHASE_POWER, id),
                createAttribute(id, StandardNames.NOVELTY_SEEKING, 2 * id),
                createAttribute(id, StandardNames.DEPENDENT_JUDGMENT, 3 * id),
                createAttribute(id, StandardNames.ENVIRONMENTAL_CONCERN, 4 * id),
                createBooleanAttribute(id, StandardNames.HOUSE_SHARE, 5 * id),
                createBooleanAttribute(id, StandardNames.OWNERSHIP_STATUS, 6 * id),
                createAttribute(id, StandardNames.CONSTRUCTION_RATE, 7 * id),
                createAttribute(id, StandardNames.RENOVATION_RATE, 8 * id)
        };
        return grp;
    }

    @Override
    public IRoot createDefaultScenario() {
        IConsumerAgentGroup grp1 = createGroup(1, "TestGroup1", 10, 1.0);
        IConsumerAgentGroup grp2 = createGroup(2, "TestGroup2", 5, 0.5);

        IBasicAffinityMapping affinityMapping = new IBasicAffinityMapping();
        affinityMapping._name = "MyAffinity";
        affinityMapping.affinityEntries = new IBasicAffinitiesEntry[] {
                new IBasicAffinitiesEntry("TestGroup1-TestGroup1", grp1, grp1, 0.7),
                new IBasicAffinitiesEntry("TestGroup1-TestGroup2", grp1, grp2, 0.3),
                new IBasicAffinitiesEntry("TestGroup2-TestGroup2", grp2, grp2, 0.9),
                new IBasicAffinitiesEntry("TestGroup2-TestGroup1", grp2, grp1, 0.1)
        };

        //Product
        IProductGroup pv = new IProductGroup();
        pv._name = "PV";
        pv.pgAttributes = new IProductGroupAttribute[] {
                new IProductGroupAttribute(
                        StandardNames.INVESTMENT_COST_UNIT_DEVELOPMENT,
                        new IRandomBoundedDistribution("E1_dist", 0, 1000, 1)
                )
        };
        IFixedProduct testPv = new IFixedProduct();
        testPv._name = "PV-Testproduct";
        testPv.fpGroup = pv;
        testPv.fpAttributes = new IFixedProductAttribute[] {
                new IFixedProductAttribute(StandardNames.INVESTMENT_COST_UNIT_DEVELOPMENT + "_1", pv.pgAttributes[0], 1234)
        };
        IFixedProductAwareness[] awareness = {
                new IFixedProductAwareness("Awareness1", grp1, testPv, new IRandomBoundedDistribution("Awareness1_dist", 0, 10, 1)),
                new IFixedProductAwareness("Awareness2", grp2, testPv, new IRandomBoundedDistribution("Awareness2_dist", 0, 10, 2))
        };

        ISpace2D space2D = new ISpace2D("MySpace2D", Metric2D.EUCLIDEAN.id());

        ITimeModel timeModel = new IDiscreteTimeModel("DiscreteTime", 1L, 900000L, 86400000L);

        IFastHeterogeneousSmallWorldTopology topology = new IFastHeterogeneousSmallWorldTopology();
        topology._name = "MyWorld";
        topology.edgeType = SocialGraph.Type.COMMUNICATION.id();
        topology.initialWeight = 1.0;
        topology.isSelfReferential = false;
        topology.topoSeed = 312;
        topology.topoEntries = new IFastHeterogeneousSmallWorldTopologyEntry[] {
                new IFastHeterogeneousSmallWorldTopologyEntry("TestGroup1-Topology", grp1, 0.01, 10),
                new IFastHeterogeneousSmallWorldTopologyEntry("TestGroup2-Topology", grp2, 0.02, 5)
        };

        IGeneralSettings generalSettings = new IGeneralSettings();
        generalSettings.debugLevel = DebugLevel.TRACE.id();

        IRoot root = new IRoot();
        root.consumerAgentGroups = new IConsumerAgentGroup[]{grp1, grp2};
        root.affinityMapping = new IBasicAffinityMapping[]{affinityMapping};
        root.productGroups = new IProductGroup[]{pv};
        root.fixedProducts = new IFixedProduct[]{testPv};
        root.fixedProductAwarenesses = awareness;
        root.spatialModel = new ISpace2D[]{space2D};
        root.timeModel = new ITimeModel[]{timeModel};
        root.topology = new IFastHeterogeneousSmallWorldTopology[]{topology};
        root.generalSettings = generalSettings;
        return root;
    }
}
