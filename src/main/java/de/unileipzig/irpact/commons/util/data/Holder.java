package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public class Holder<T> {

    private final T entity;

    public Holder(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Holder)) return false;
        Holder<?> holder = (Holder<?>) o;
        return entity == holder.entity;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(entity);
    }
}
