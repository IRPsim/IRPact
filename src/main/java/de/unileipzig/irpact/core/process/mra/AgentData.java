package de.unileipzig.irpact.core.process.mra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface AgentData {

    RAStage getStage();

    Need getNeed();

    void updateStage(RAStage stage);

    Rnd getRnd();

    Product getProduct();

    default String getProductName() {
        return getProduct().getName();
    }

    boolean isUnderConstruction();

    boolean isUnderRenovation();
}
