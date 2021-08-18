package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNSubModules;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SocialComponentModule extends AbstractConsumerAgentModuleWithNSubModules implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SocialComponentModule.class);

    protected static final int NUMBER_OF_MODULES = 2;
    protected static final int INDEX_SOCIAL_MODULE = 0;
    protected static final int INDEX_LOCAL_MODULE = 1;

    public SocialComponentModule() {
        super(NUMBER_OF_MODULES);
        setSubModule(INDEX_SOCIAL_MODULE, new ShareOfAdopterInSocialNetworkModule());
        setSubModule(INDEX_LOCAL_MODULE, new ShareOfAdopterInLocalNetworkModule());
        getSocialNetworkSubModule().setWeight(1.0);
        getLocalNetworkSubModule().setWeight(1.0);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getWeight()
        );
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        getSocialNetworkSubModule().setName(name + "_socialNetworkSubModule");
        getLocalNetworkSubModule().setName(name + "_localNetworkSubModule");
    }

    protected double weight = 1.0;
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    protected double socialWeight = 0.5;
    public void setSocialWeight(double socialWeight) {
        this.socialWeight = socialWeight;
    }
    public double getSocialWeight() {
        return socialWeight;
    }

    protected double localWeight = 0.5;
    public void setLocalWeight(double localWeight) {
        this.localWeight = localWeight;
    }
    public double getLocalWeight() {
        return localWeight;
    }

    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        getLocalNetworkSubModule().setNodeFilterScheme(nodeFilterScheme);
    }
    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return getLocalNetworkSubModule().getNodeFilterScheme();
    }

    public ShareOfAdopterInSocialNetworkModule getSocialNetworkSubModule() {
        return getValidSubModuleAs(INDEX_SOCIAL_MODULE);
    }

    public ShareOfAdopterInLocalNetworkModule getLocalNetworkSubModule() {
        return getValidSubModuleAs(INDEX_LOCAL_MODULE);
    }

    @Override
    public double calculate(ConsumerAgentData input) {
        double localPart = getLocalNetworkSubModule().calculate(input);
        double socialPart = getSocialNetworkSubModule().calculate(input);

        double weightedLocal = localPart * getLocalWeight();
        double weightedSocial = socialPart * getSocialWeight();

        return getWeight() * (weightedLocal + weightedSocial);
    }
}
