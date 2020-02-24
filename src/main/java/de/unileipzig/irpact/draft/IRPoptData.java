package de.unileipzig.irpact.draft;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
public class IRPoptData {

    private Map<String, KundenGruppe> set_side_cust = new HashMap<>();

    public void put(String kg, String key, double value) {
        set_side_cust.get(kg)
                .put(key, value);
    }
}
