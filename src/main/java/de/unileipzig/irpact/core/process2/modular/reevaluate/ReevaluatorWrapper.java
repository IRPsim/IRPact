package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ReevaluatorWrapper<I> extends AbstractReevaluator<I> {

    protected Reevaluator<I> reevaluator;

    public void setReevaluator(Reevaluator<I> reevaluator) {
        this.reevaluator = reevaluator;
    }

    public Reevaluator<I> getReevaluator() {
        return reevaluator;
    }

    @Override
    public void reevaluate(I input, List<PostAction2> actions) throws Throwable {
        reevaluator.reevaluate(input, actions);
    }
}
