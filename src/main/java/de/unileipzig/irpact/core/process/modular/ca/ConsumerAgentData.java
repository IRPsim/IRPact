package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.modular.InputData;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentData extends InputData {

    ProcessPlan getPlan();

    ConsumerAgent getAgent();

    Product getProduct();

    Need getNeed();

    Rnd rnd();

    Stage currentStage();
    void updateStage(Stage newStage);

    boolean isUnderConstruction();
    void setUnderConstruction(boolean value);

    boolean isUnderRenovation();
    void setUnderRenovation(boolean value);
}
