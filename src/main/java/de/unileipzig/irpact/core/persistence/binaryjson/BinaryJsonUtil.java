package de.unileipzig.irpact.core.persistence.binaryjson;

import de.unileipzig.irpact.jadex.persistance.binaryjson.data.*;

/**
 * @author Daniel Abitz
 */
final class BinaryJsonUtil {

    private BinaryJsonUtil() {
    }

    static void registerDefaults(BinaryJsonPersistanceManager persistanceManager) {
        ensureRegisterDefaults(persistanceManager);
    }

    static void registerDefaults(BinaryJsonRestoreManager restoreManager) {
        ensureRegisterDefaults(restoreManager);
    }

    private static void ensureRegisterDefaults(Object manager) {
        ensureRegister(manager, AttitudeGapRelativeAgreementAlgorithmPR.INSTANCE);
        ensureRegister(manager, BasicAdoptedProductPR.INSTANCE);
        ensureRegister(manager, BasicAgentManagerPR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentAnnualAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentAnnualGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentDoubleAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentGroupAffinityMappingPR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentDoubleGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentProductRelatedAttributePR.INSTANCE);
        ensureRegister(manager, BasicConsumerAgentProductRelatedGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicDistanceEvaluatorPR.INSTANCE);
        ensureRegister(manager, BasicDoubleRangePR.INSTANCE);
        ensureRegister(manager, BasicUncertaintyManagerPR.INSTANCE);
        ensureRegister(manager, BasicEdgePR.INSTANCE);
        ensureRegister(manager, BasicJadexLifeCycleControlPR.INSTANCE);
        ensureRegister(manager, BasicJadexSimulationEnvironmentPR.INSTANCE);
        ensureRegister(manager, BasicMultiConsumerAgentGroupAttributeSupplierPR.INSTANCE);
        ensureRegister(manager, BasicNeedPR.INSTANCE);
        ensureRegister(manager, BasicPoint2DPR.INSTANCE);
        ensureRegister(manager, BasicProcessModelManagerPR.INSTANCE);
        ensureRegister(manager, BasicProductAttributePR.INSTANCE);
        ensureRegister(manager, BasicProductGroupAttributePR.INSTANCE);
        ensureRegister(manager, BasicProductGroupPR.INSTANCE);
        ensureRegister(manager, BasicProductManagerPR.INSTANCE);
        ensureRegister(manager, BasicProductPR.INSTANCE);
        ensureRegister(manager, BasicSettingsPR.INSTANCE);
        ensureRegister(manager, BasicSocialGraphPR.INSTANCE);
        ensureRegister(manager, BasicSocialNetworkPR.INSTANCE);
        ensureRegister(manager, BasicVersionPR.INSTANCE);
        ensureRegister(manager, BernoulliDistributionPR.INSTANCE);
        ensureRegister(manager, BooleanDistributionPR.INSTANCE);
        ensureRegister(manager, BoundedNormalDistributionPR.INSTANCE);
        ensureRegister(manager, BoundedUniformDoubleDistributionPR.INSTANCE);
        ensureRegister(manager, BoundedUniformIntegerDistributionPR.INSTANCE);
        ensureRegister(manager, CeilingTimeAdvanceFunctionPR.INSTANCE);
        ensureRegister(manager, CompleteGraphTopologyPR.INSTANCE);
        ensureRegister(manager, DefaultDoActionComponentPR.INSTANCE);
        ensureRegister(manager, DefaultHandleDecisionMakingComponentPR.INSTANCE);
        ensureRegister(manager, DefaultHandleFeasibilityComponentPR.INSTANCE);
        ensureRegister(manager, DefaultHandleInterestComponentPR.INSTANCE);
        ensureRegister(manager, DeffuantUncertaintyPR.INSTANCE);
        ensureRegister(manager, DiracUnivariateDoubleDistributionPR.INSTANCE);
        ensureRegister(manager, DisabledNodeFilterPR.INSTANCE);
        ensureRegister(manager, DisabledProcessPlanNodeFilterSchemePR.INSTANCE);
        ensureRegister(manager, DiscreteTimeModelPR.INSTANCE);
        ensureRegister(manager, EntireNetworkNodeFilterPR.INSTANCE);
        ensureRegister(manager, EntireNetworkNodeFilterSchemePR.INSTANCE);
        ensureRegister(manager, FixProcessModelFindingSchemePR.INSTANCE);
        ensureRegister(manager, FixProductFindingSchemePR.INSTANCE);
        ensureRegister(manager, FreeNetworkTopologyPR.INSTANCE);
        ensureRegister(manager, GlobalDeffuantUncertaintyDataPR.INSTANCE);
        ensureRegister(manager, GlobalDeffuantUncertaintySupplierPR.INSTANCE);
        ensureRegister(manager, GroupBasedDeffuantUncertaintyDataPR.INSTANCE);
        ensureRegister(manager, GroupBasedDeffuantUncertaintySupplierPR.INSTANCE);
        ensureRegister(manager, InversePR.INSTANCE);
        ensureRegister(manager, JadexConsumerAgentGroupPR.INSTANCE);
        ensureRegister(manager, MaxDistanceNodeFilterPR.INSTANCE);
        ensureRegister(manager, ModularRAProcessModelPR.INSTANCE);
        ensureRegister(manager, ModularRAProcessPlanPR.INSTANCE);
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
        ensureRegister(manager, RAProcessModelPR.INSTANCE);
        ensureRegister(manager, RAProcessPlanMaxDistanceFilterSchemePR.INSTANCE);
        ensureRegister(manager, RAProcessPlanPR.INSTANCE);
        ensureRegister(manager, RndPR.INSTANCE);
        ensureRegister(manager, Space2DPR.INSTANCE);
        ensureRegister(manager, SpatialDoubleAttributeBasePR.INSTANCE);
        ensureRegister(manager, SpatialInformationSupplierPR.INSTANCE);
        ensureRegister(manager, SpatialStringAttributeBasePR.INSTANCE);
        ensureRegister(manager, TruncatedNormalDistributionPR.INSTANCE);
        ensureRegister(manager, UnitStepDiscreteTimeModelPR.INSTANCE);
        ensureRegister(manager, UnlinkedGraphTopologyPR.INSTANCE);
    }

    private static void ensureRegister(Object manager, Object impl) {
        if(manager instanceof BinaryJsonPersistanceManager) {
            BinaryJsonPersistanceManager persistanceManager = (BinaryJsonPersistanceManager) manager;
            BinaryPersister<?> persister = (BinaryPersister<?>) impl;
            persistanceManager.ensureRegister(persister);
        } else {
            BinaryJsonRestoreManager restoreManager = (BinaryJsonRestoreManager) manager;
            BinaryRestorer<?> restorer = (BinaryRestorer<?>) impl;
            restoreManager.ensureRegister(restorer);
        }
    }
}
