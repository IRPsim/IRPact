package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractVoidModule2<I>
        extends AbstractModule2<I, Void>
        implements VoidModule2<I>, LoggingHelper {

    @Override
    public Void apply(I input, List<PostAction2> actions) throws Throwable {
        applyVoid(input, actions);
        return null;
    }

    public abstract void applyVoid(I input, List<PostAction2> actions);
}
