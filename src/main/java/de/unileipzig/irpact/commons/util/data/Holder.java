package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public class Holder<T> {

    private final T ENTITY;

    public Holder(T entity) {
        this.ENTITY = entity;
    }

    public T getEntity() {
        return ENTITY;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Holder)) return false;
        Holder<?> holder = (Holder<?>) o;
        return ENTITY == holder.ENTITY;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(ENTITY);
    }
}
