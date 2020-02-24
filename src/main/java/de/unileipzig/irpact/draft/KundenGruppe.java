package de.unileipzig.irpact.draft;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
public class KundenGruppe {

    private Map<String, Double> par_OH_DES_ES_side = new HashMap<>();

    public void put(String key, double value) {
        par_OH_DES_ES_side.put(key, value);
    }
}
