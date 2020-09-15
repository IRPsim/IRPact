package de.unileipzig.irpact.start.irpact;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.distribution.RandomBoundedDistribution;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroup;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroupAttribute;
import de.unileipzig.irpact.start.irpact.input.simulation.ContinousTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.DiscretTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.TimeModel;

/**
 * @author Daniel Abitz
 */
public class DefaultScenarioFactory {

    private DefaultScenarioFactory() {
    }

    public static IRPactInputData createContinousInputData() {
        Need needPV = new Need("needPV");
        Need needPC = new Need("needPC");

        AgentGroup aGrpPV = new AgentGroup("Group-PV", 50, 0.01, 1, new Need[]{needPV});
        AgentGroup aGrpPC = new AgentGroup("Group-PC", 50, 0.01, 2, new Need[]{needPC});
        AgentGroup aGrpPVPC = new AgentGroup("Group-PVPC", 100, 0.005, 3, new Need[]{needPV, needPC});

        RandomBoundedDistribution distPV = new RandomBoundedDistribution("DistPV", 42, 100, 200);
        ProductGroupAttribute pgAttrPricePV = new ProductGroupAttribute("PricePV", distPV);
        ProductGroup gPV = new ProductGroup(
                "PV",
                new ProductGroupAttribute[]{pgAttrPricePV},
                new Need[]{needPV}
        );
        FixedProduct fixedPV = new FixedProduct(
                "FixedPV",
                gPV,
                CollectionUtil.hashMapOf(pgAttrPricePV, 150.0)
        );

        RandomBoundedDistribution distPC = new RandomBoundedDistribution("DistPC", 1337, 500, 1000);
        ProductGroupAttribute pgAttrPricePC = new ProductGroupAttribute("PricePC", distPC);
        ProductGroup gPC = new ProductGroup(
                "PC",
                new ProductGroupAttribute[]{pgAttrPricePC},
                new Need[]{needPC}
        );
        FixedProduct fixedPC = new FixedProduct(
                "FixedPC",
                gPC,
                CollectionUtil.hashMapOf(pgAttrPricePC, 750.0)
        );

        int acceleration = 60 * 60 * 24 * 31; //1 real-sec = 1 simu-Monat
        long delay = 1000L * 60L * 60L * 24L * 7L; //1 Simu-Wocht
        ContinousTimeModel ctm = new ContinousTimeModel("ContTime", acceleration, delay);

        return new IRPactInputData(
                new AgentGroup[]{aGrpPV, aGrpPC, aGrpPVPC},
                new TimeModel[]{ctm},
                new ProductGroup[]{gPV, gPC},
                new FixedProduct[]{fixedPV, fixedPC}
        );
    }

    public static IRPactInputData createDiscreteInputData() {
        IRPactInputData data = createContinousInputData();

        int msPerTick = 1000 * 60 * 60 * 24; //1 Tag pro Tick
        long delay = 1000L * 60L * 60L * 24L * 7L; //1 Simu-Wocht
        DiscretTimeModel dtm = new DiscretTimeModel("DiscretTime", msPerTick, delay);
        data.timeModels[0] = dtm;

        return data;
    }

    private static IRPactInputData updateGroups(IRPactInputData data, int x) {
        if(x == 1) {
            AgentGroup group = data.agentGroups[0];
            group.numberOfAgents = 1;
            data.agentGroups = new AgentGroup[]{group};
            return data;
        }
        if(x == 2) {
            AgentGroup group1 = data.agentGroups[0];
            group1.numberOfAgents = 1;
            AgentGroup group2 = data.agentGroups[1];
            group2.numberOfAgents = 1;
            data.agentGroups = new AgentGroup[]{group1, group2};
            return data;
        }
        if(x == 3) {
            AgentGroup group1 = data.agentGroups[0];
            group1.numberOfAgents = 1;
            AgentGroup group2 = data.agentGroups[1];
            group2.numberOfAgents = 1;
            AgentGroup group3 = data.agentGroups[2];
            group3.numberOfAgents = 1;
            data.agentGroups = new AgentGroup[]{group1, group2, group3};
            return data;
        }
        throw new IllegalArgumentException();
    }

    public static IRPactInputData createContinousInputDataForTest(int x) {
        IRPactInputData data = createContinousInputData();
        return updateGroups(data, x);
    }

    public static IRPactInputData createDiscreteInputDataForTest(int x) {
        IRPactInputData data = createDiscreteInputData();
        return updateGroups(data, x);
    }
}
