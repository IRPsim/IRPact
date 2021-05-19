package de.unileipzig.irpact.util.sensitivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public enum IntersectionMode {
    LOCKSTEP {

        @Override
        public <I> void run(
                Supplier<? extends I> inputCreator,
                Function<? super I, ? extends I> copyFunction,
                Consumer<? super I> executor,
                int steps,
                Collection<? extends Sensitifity<? super I>> sensitifities) {
            I original = inputCreator.get();
            I next = original;
            for(int i = 0; i < steps; i++) {
                if(i > 0) next = copyFunction.apply(original);
                for(Sensitifity<? super I> sensitifity: sensitifities) {
                    double value = sensitifity.getDelta(steps, i);
                    sensitifity.apply(next, value);
                }
                executor.accept(next);
            }
        }
    },
    CROSSPRODUCT {

        @Override
        public <I> void run(
                Supplier<? extends I> inputCreator,
                Function<? super I, ? extends I> copyFunction,
                Consumer<? super I> executor,
                int steps,
                Collection<? extends Sensitifity<? super I>> sensitifities) {
            I original = inputCreator.get();
            List<? extends Sensitifity<? super I>> list = asList(sensitifities);
            recRun(original, copyFunction, executor, steps, list, 0);
        }

        @SuppressWarnings("unchecked")
        private <I> List<? extends Sensitifity<? super I>> asList(Collection<? extends Sensitifity<? super I>> sensitifities) {
            return sensitifities instanceof List
                    ? (List<? extends Sensitifity<? super I>>) sensitifities
                    : new ArrayList<>(sensitifities);
        }

        private <I> void recRun(
                I input,
                Function<? super I, ? extends I> copyFunction,
                Consumer<? super I> executor,
                int steps,
                List<? extends Sensitifity<? super I>> list,
                final int listIndex) {
            if(listIndex < list.size()) {
                Sensitifity<? super I> sensitifity = list.get(listIndex);
                double[] values = sensitifity.getDeltas(steps);
                I original = copyFunction.apply(input);
                I next = input;
                for(int i = 0; i < values.length; i++) {
                    if(i > 0) next = copyFunction.apply(original);
                    sensitifity.apply(next, values[i]);
                    recRun(next, copyFunction, executor, steps, list, listIndex + 1);
                }
            } else {
                executor.accept(input);
            }
        }
    };

    public abstract <I> void run(
            Supplier<? extends I> inputCreator,
            Function<? super I, ? extends I> copyFunction,
            Consumer<? super I> executor,
            int steps,
            Collection<? extends Sensitifity<? super I>> sensitifities);
}
