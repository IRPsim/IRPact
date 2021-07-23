package de.unileipzig.irpact.core.process.mra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.product.Product;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface AgentData {

    RAStage getStage();

    Need getNeed();

    void setStage(RAStage stage);

    Rnd getRnd();

    Product getProduct();

    default String getProductName() {
        return getProduct().getName();
    }

    boolean isUnderConstruction();

    boolean isUnderRenovation();

    void store(String key, Object value);

    Object get(String key);

    @SuppressWarnings("unchecked")
    default <R> R getAs(String key) {
        return (R) get(key);
    }

    default <R> R getExistingAs(String key) {
        R r = getAs(key);
        if(r == null) {
            throw new NoSuchElementException(Objects.toString(key));
        }
        return r;
    }

    boolean has(String key);
}
