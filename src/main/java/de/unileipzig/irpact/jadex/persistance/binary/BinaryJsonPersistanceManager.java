package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.BasicPersistManager;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.Persister;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.jadex.agents.simulation.JadexSimulationAgentBDI;
import de.unileipzig.irpact.jadex.persistance.binary.impl.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class BinaryJsonPersistanceManager extends BasicPersistManager {

    public BinaryJsonPersistanceManager() {
        init();
    }

    private void init() {
        ensureRegister(BasicAdoptedProductPR.INSTANCE);
        ensureRegister(BasicConsumerAgentAttributePR.INSTANCE);
        ensureRegister(BasicConsumerAgentGroupAffinityMappingPR.INSTANCE);
        ensureRegister(BasicConsumerAgentGroupAttributePR.INSTANCE);
        ensureRegister(BasicConsumerAgentSpatialAttributeSupplierPR.INSTANCE);
        ensureRegister(BasicDistanceEvaluatorPR.INSTANCE);
        ensureRegister(BasicEdgePR.INSTANCE);
        ensureRegister(BasicJadexLifeCycleControlPR.INSTANCE);
        ensureRegister(BasicJadexSimulationEnvironmentPR.INSTANCE);
        ensureRegister(BasicNeedPR.INSTANCE);
        ensureRegister(BasicPoint2DPR.INSTANCE);
        ensureRegister(BasicProductAttributePR.INSTANCE);
        ensureRegister(BasicProductGroupAttributePR.INSTANCE);
        ensureRegister(BasicProductGroupPR.INSTANCE);
        ensureRegister(BasicProductPR.INSTANCE);
        ensureRegister(BasicSocialGraphPR.INSTANCE);
        ensureRegister(BasicVersionPR.INSTANCE);
        ensureRegister(BooleanDistributionPR.INSTANCE);
        ensureRegister(CompleteGraphTopologyPR.INSTANCE);
        ensureRegister(ConstantSpatialDistributionPR.INSTANCE);
        ensureRegister(ConstantUnivariateDoubleDistributionPR.INSTANCE);
        ensureRegister(DiscreteTimeModelPR.INSTANCE);
        ensureRegister(FixProcessModelFindingSchemePR.INSTANCE);
        ensureRegister(FixProductFindingSchemePR.INSTANCE);
        ensureRegister(FreeNetworkTopologyPR.INSTANCE);
        ensureRegister(InversePR.INSTANCE);
        ensureRegister(JadexConsumerAgentGroupPR.INSTANCE);
        ensureRegister(NoDistancePR.INSTANCE);
        ensureRegister(ProductThresholdAwarenessPR.INSTANCE);
        ensureRegister(ProductThresholdAwarenessSupplySchemePR.INSTANCE);
        ensureRegister(ProxyConsumerAgentPR.INSTANCE);
        ensureRegister(ProxySimulationAgentPR.INSTANCE);
        ensureRegister(RADataSupplierPR.INSTANCE);
        ensureRegister(RAModelDataPR.INSTANCE);
        ensureRegister(RandomBoundedIntegerDistributionPR.INSTANCE);
        ensureRegister(RAProcessModelPR.INSTANCE);
        ensureRegister(RAProcessPlanPR.INSTANCE);
        ensureRegister(RndPR.INSTANCE);
        ensureRegister(Space2DPR.INSTANCE);
        ensureRegister(SpatialDoubleAttributeBasePR.INSTANCE);
        ensureRegister(SpatialStringAttributeBasePR.INSTANCE);
        ensureRegister(UncertaintyAttributePR.INSTANCE);
        ensureRegister(UncertaintyGroupAttributePR.INSTANCE);
        ensureRegister(UnlinkedGraphTopologyPR.INSTANCE);
        ensureRegister(WeightedDiscreteSpatialDistributionPR.INSTANCE);
    }

    //=========================
    //util
    //=========================

    public static BinaryJsonData initData(Object obj, PersistManager manager) {
        return initDataWithClass(obj.getClass(), manager);
    }

    public static BinaryJsonData initDataWithClass(Class<?> c, PersistManager manager) {
        return BinaryJsonData.init(IRPactJson.SMILE.getNodeFactory(), manager.newUID(), c);
    }
}
