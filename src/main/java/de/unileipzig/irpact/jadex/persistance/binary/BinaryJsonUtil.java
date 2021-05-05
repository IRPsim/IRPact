package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.distribution.BernoulliDistribution;
import de.unileipzig.irpact.commons.persistence.Persister;
import de.unileipzig.irpact.commons.persistence.Restorer;
import de.unileipzig.irpact.jadex.persistance.binary.impl.*;

/**
 * @author Daniel Abitz
 */
public final class BinaryJsonUtil {

    private BinaryJsonUtil() {
    }

    public static void registerDefaults(BinaryJsonPersistanceManager persistanceManager) {
        ensureRegisterDefaults(persistanceManager);
    }

    public static void registerDefaults(BinaryJsonRestoreManager restoreManager) {
        ensureRegisterDefaults(restoreManager);
    }

    private static void ensureRegisterDefaults(Object manager) {
        ensureRegister(manager, BasicAdoptedProductPR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentAnnualAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentAnnualGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentDoubleAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentGroupAffinityMappingPR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentDoubleGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentProductRelatedAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentProductRelatedGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicDistanceEvaluatorPR.INSTANCE);
        ensureRegister(manager, BasicEdgePR.INSTANCE);
        ensureRegister(manager, BasicJadexLifeCycleControlPR.INSTANCE);
        ensureRegister(manager, BasicJadexSimulationEnvironmentPR.INSTANCE);
        ensureRegister(manager, BasicMultiConsumerAgentGroupAttributeSupplierPR.INSTANCE);
        ensureRegister(manager, BasicNeedPR.INSTANCE);
        ensureRegister(manager, BasicPoint2DPR.INSTANCE);
        ensureRegister(manager, BasicProductAttributePR.INSTANCE);
        ensureRegister(manager, BasicProductGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicProductGroupPR.INSTANCE);
        ensureRegister(manager, BasicProductPR.INSTANCE);
        ensureRegister(manager, BasicSocialGraphPR.INSTANCE);
        ensureRegister(manager, BasicUncertaintyAttributePR.INSTANCE);
        ensureRegister(manager, BasicUncertaintyGroupAttributeSupplierPR.INSTANCE);
        ensureRegister(manager, BasicVersionPR.INSTANCE);
        ensureRegister(manager, BernoulliDistributionPR.INSTANCE);
        ensureRegister(manager, BooleanDistributionPR.INSTANCE);
        ensureRegister(manager, CeilingTimeAdvanceFunctionPR.INSTANCE);
        ensureRegister(manager, CompleteGraphTopologyPR.INSTANCE);
        ensureRegister(manager, ConstantUnivariateDoubleDistributionPR.INSTANCE);
        ensureRegister(manager, DisabledNodeFilterPR.INSTANCE);
        ensureRegister(manager, DisabledProcessPlanNodeFilterSchemePR.INSTANCE);
        ensureRegister(manager, DiscreteSpatialDistributionPR.INSTANCE);
        ensureRegister(manager, DiscreteTimeModelPR.INSTANCE);
        ensureRegister(manager, EntireNetworkNodeFilterPR.INSTANCE);
        ensureRegister(manager, EntireNetworkNodeFilterSchemePR.INSTANCE);
        ensureRegister(manager, FixProcessModelFindingSchemePR.INSTANCE);
        ensureRegister(manager, FixProductFindingSchemePR.INSTANCE);
        ensureRegister(manager, FreeNetworkTopologyPR.INSTANCE);
        ensureRegister(manager, InversePR.INSTANCE);
        ensureRegister(manager, JadexConsumerAgentGroupPR.INSTANCE);
        ensureRegister(manager, LinkedUncertaintyGroupAttributePR.INSTANCE);
        ensureRegister(manager, MaxDistanceNodeFilterPR.INSTANCE);
        ensureRegister(manager, NoDistancePR.INSTANCE);
        ensureRegister(manager, NormalDistributionPR.INSTANCE);
        ensureRegister(manager, ProductBinaryAwarenessPR.INSTANCE);
        ensureRegister(manager, ProductBinaryAwarenessSupplySchemePR.INSTANCE);
        ensureRegister(manager, ProductThresholdInterestPR.INSTANCE);
        ensureRegister(manager, ProductThresholdInterestSupplySchemePR.INSTANCE);
        ensureRegister(manager, ProxyConsumerAgentPR.INSTANCE);
        ensureRegister(manager, ProxySimulationAgentPR.INSTANCE);
        ensureRegister(manager, RADataSupplierPR.INSTANCE);
        ensureRegister(manager, RAModelDataPR.INSTANCE);
        ensureRegister(manager, RandomBoundedIntegerDistributionPR.INSTANCE);
        ensureRegister(manager, RAProcessModelPR.INSTANCE);
        ensureRegister(manager, RAProcessPlanMaxDistanceFilterSchemePR.INSTANCE);
        ensureRegister(manager, RAProcessPlanPR.INSTANCE);
        ensureRegister(manager, RndPR.INSTANCE);
        ensureRegister(manager, Space2DPR.INSTANCE);
        ensureRegister(manager, SpatialDoubleAttributeBasePR.INSTANCE);
        ensureRegister(manager, SpatialStringAttributeBasePR.INSTANCE);
        ensureRegister(manager, UnitStepDiscreteTimeModelPR.INSTANCE);
        ensureRegister(manager, UnlinkedGraphTopologyPR.INSTANCE);
        ensureRegister(manager, WeightedDiscreteSpatialDistributionPR.INSTANCE);
    }

    private static void ensureRegister(Object manager, Object impl) {
        if(manager instanceof BinaryJsonPersistanceManager) {
            BinaryJsonPersistanceManager persistanceManager = (BinaryJsonPersistanceManager) manager;
            Persister<?> persister = (Persister<?>) impl;
            persistanceManager.ensureRegister(persister);
        } else {
            BinaryJsonRestoreManager restoreManager = (BinaryJsonRestoreManager) manager;
            Restorer<?> restorer = (Restorer<?>) impl;
            restoreManager.ensureRegister(restorer);
        }
    }
}
