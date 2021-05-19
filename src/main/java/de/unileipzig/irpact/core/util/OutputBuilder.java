package de.unileipzig.irpact.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class OutputBuilder {

    private OutputBuilder() {
    }

    /**
     * @author Daniel Abitz
     */
    public static <A, X, R> List<R> reduce1(
            Collection<X> input,
            A aFilter,
            Function<? super X, ? extends A> aSelector,
            List<Reducer<X, R>> reducers) {

        input.stream()
                .filter(x -> {
                    A a = aSelector.apply(x);
                    return Objects.equals(a, aFilter);
                })
                .forEach(x -> {
                    for(Reducer<X, R> reducer: reducers) {
                        reducer.update(x);
                    }
                });

        return reducers.stream()
                .map(Reducer::getResult)
                .collect(Collectors.toList());
    }

    /**
     * @author Daniel Abitz
     */
    public static <A, B, X, R> List<R> reduce2(
            Collection<X> input,
            A aFilter,
            B bFilter,
            Function<? super X, ? extends A> aSelector,
            Function<? super X, ? extends B> bSelector,
            List<Reducer<X, R>> reducers) {

        input.stream()
                .filter(x -> {
                    A a = aSelector.apply(x);
                    B b = bSelector.apply(x);
                    return Objects.equals(a, aFilter) && Objects.equals(b, bFilter);
                })
                .forEach(x -> {
                    for(Reducer<X, R> reducer: reducers) {
                        reducer.update(x);
                    }
                });

        return reducers.stream()
                .map(Reducer::getResult)
                .collect(Collectors.toList());
    }

    /**
     * @author Daniel Abitz
     */
    public static class Reducer<X, U> {

        private U identity;
        private BinaryOperator<U> operator;
        private Function<? super X, ? extends U> mapper;

        private U result;

        public Reducer() {
        }

        public void setIdentity(U identity) {
            this.identity = identity;
            this.result = identity;
        }

        public U getIdentity() {
            return identity;
        }

        public void setOperator(BinaryOperator<U> operator) {
            this.operator = operator;
        }

        public BinaryOperator<U> getOperator() {
            return operator;
        }

        public void setMapper(Function<? super X, ? extends U> mapper) {
            this.mapper = mapper;
        }

        public Function<? super X, ? extends U> getMapper() {
            return mapper;
        }

        public void update(X other) {
            U u = mapper.apply(other);
            result = getOperator().apply(result, u);
        }

        public U getResult() {
            return result;
        }
    }
}
