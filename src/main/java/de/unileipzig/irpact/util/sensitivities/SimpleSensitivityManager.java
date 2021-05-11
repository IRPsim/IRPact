package de.unileipzig.irpact.util.sensitivities;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class SimpleSensitivityManager<I> implements SensitivityManager<I> {

    protected IntersectionMode mode;
    protected Supplier<? extends I> creator;
    protected Function<? super I, ? extends I> copy;
    protected Consumer<? super I> executor;

    public SimpleSensitivityManager(IntersectionMode mode) {
        this.mode = mode;
    }

    public void setCreator(Supplier<? extends I> creator) {
        this.creator = creator;
    }

    public void setCopy(Function<? super I, ? extends I> copy) {
        this.copy = copy;
    }

    public void setExecutor(Consumer<? super I> executor) {
        this.executor = executor;
    }

    @Override
    public void run(int steps, Collection<? extends Sensitifity<? super I>> sensitifities) {
        mode.run(creator, copy, executor, steps, sensitifities);
    }
}
