package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public final class ToyModelUtil {

    public static final BiConsumer<InRoot, OutRoot> IGNORE = (inRoot, outRoot) -> {};

    private ToyModelUtil() {
    }
}
