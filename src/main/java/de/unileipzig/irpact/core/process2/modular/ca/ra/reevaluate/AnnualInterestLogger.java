package de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate;

import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.AbstractReevaluator;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AnnualInterestLogger
        extends AbstractReevaluator<ConsumerAgentData2>
        implements RAHelperAPI2 {

    @Override
    public SharedModuleData getSharedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) {
        logAnnualInterest(input);
    }
}
