package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface Reevaluator<I> extends Nameable {

    void reevaluate(I input, List<PostAction2> actions) throws Throwable;
}
