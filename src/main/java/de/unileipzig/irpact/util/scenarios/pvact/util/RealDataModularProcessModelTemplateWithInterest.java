package de.unileipzig.irpact.util.scenarios.pvact.util;

import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactDefaultAwarenessHandler;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactDependentInterestScaler;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.util.scenarios.util.AttributeNameManager;
import de.unileipzig.irpact.util.scenarios.util.ModuleManager;

/**
 * @author Daniel Abitz
 */
public class RealDataModularProcessModelTemplateWithInterest extends RealDataModularProcessModelTemplate {

    protected String initialAwarenessUpdaterName = "AWARENESS_INITIALIZER";
    public void setInitialAwarenessUpdaterName(String initialAwarenessUpdaterName) {
        this.initialAwarenessUpdaterName = initialAwarenessUpdaterName;
    }
    public String getInitialAwarenessUpdaterName() {
        return initialAwarenessUpdaterName;
    }

    protected String initialInterestUpdaterName = "INTEREST_INITIALIZER";
    public void setInitialInterestUpdaterName(String initialInterestUpdaterName) {
        this.initialInterestUpdaterName = initialInterestUpdaterName;
    }
    public String getInitialInterestUpdaterName() {
        return initialInterestUpdaterName;
    }

    protected InUnivariateDoubleDistribution interestDistribution;
    public void setInterestDistribution(InUnivariateDoubleDistribution interestDistribution) {
        this.interestDistribution = interestDistribution;
    }
    public InUnivariateDoubleDistribution getInterestDistribution() {
        return interestDistribution;
    }

    public RealDataModularProcessModelTemplateWithInterest(String mpmName) {
        super(mpmName);
    }

    public RealDataModularProcessModelTemplateWithInterest(
            ModuleManager mm,
            AttributeNameManager anm,
            String mpmName) {
        super(mm, anm, mpmName);
    }

    @Override
    protected void setupInitialAdopter() {
        useRealDataBasedInitialAdopterWithInterest();
    }

    public void useRealDataBasedInitialAdopterWithInterest() {
        setApplyNewProductHandler(_mpm -> {
            InPVactDefaultAwarenessHandler awa = new InPVactDefaultAwarenessHandler();
            awa.setName(getInitialAwarenessUpdaterName());
            awa.setPriority(1);

            InPVactDependentInterestScaler interest = new InPVactDependentInterestScaler();
            interest.setName(getInitialInterestUpdaterName());
            interest.setDistribution(getInterestDistribution());
            interest.setPriority(2);

            InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData initAdopter = createInPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData();
            initAdopter.setPriority(3);

            _mpm.addNewProductHandlers(awa, interest, initAdopter);
        });
    }
}
