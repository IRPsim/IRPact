package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.modular.InputData;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentData extends InputData {

    void lock();

    void unlock();

    ConsumerAgentMPM getModel();

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

    boolean has(String key);
    void store(String key, Object obj);
    Object retrieve(String key);
    <R> R retrieveAs(String key, Class<R> type);
}
