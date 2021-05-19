package de.unileipzig.irpact.util.sensitivities;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface SensitivityManager<I> {

    void run(int steps, Collection<? extends Sensitifity<? super I>> sensitifities);
}
