package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.Distribution;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCollection;

import java.util.*;

/**
 * Implements a distribution based on {@link DataCollection} and {@link DataCollection.View}.
 *
 * @param <T> intermediate type to select the view
 * @param <R> type of drawn values (return type)
 *
 * @author Daniel Abitz
 */
public class WeightedDataSupplier<T, R> extends NameableBase implements Distribution<R> {

    protected DataCollection<R> data;
    protected Map<T, DataCollection.View<R>> views;
    protected WeightedMapping<T> mapping;
    protected Rnd rnd;
    protected boolean removeOnDraw = false;

    public WeightedDataSupplier() {
        this(new NavigableMapWeightedMapping<>(), new LinkedHashMap<>());
    }

    public WeightedDataSupplier(WeightedMapping<T> mapping, Map<T, DataCollection.View<R>> views) {
        this.mapping = mapping;
        this.views = views;
    }

    protected void rebuildWeightedMapping(boolean skipEmptyViews) {
        mapping.clear();
        for(Map.Entry<T, DataCollection.View<R>> entry: views.entrySet()) {
            DataCollection.View<R> view = entry.getValue();
            if(skipEmptyViews && view.isEmpty()) {
                continue;
            }
            mapping.set(entry.getKey(), view.size());
        }
    }

    protected void checkForChange() {
        List<T> toRemove = new ArrayList<>();
        for(Map.Entry<T, DataCollection.View<R>> entry: views.entrySet()) {
            if(entry.getValue().isEmpty()) {
                toRemove.add(entry.getKey());
            }
        }

        for(T t: toRemove) {
            views.remove(t);
        }
        mapping.removeAll(toRemove);
    }

    public void setRemoveOnDraw(boolean removeOnDraw) {
        this.removeOnDraw = removeOnDraw;
    }

    public boolean isRemoveOnDraw() {
        return removeOnDraw;
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setData(DataCollection<R> data) {
        this.data = data;
    }

    public DataCollection<R> getData() {
        return data;
    }

    public void set(T key, DataCollection.Filter<? super R> filter) {
        if(views.containsKey(key)) {
            throw new IllegalArgumentException("key already exists: " + key);
        }

        DataCollection.View<R> view = data.createView(filter);
        views.put(key, view);
        mapping.set(key, view.size());
    }

    protected boolean hasView(T key) {
        return views.containsKey(key);
    }

    protected void putView(T key, DataCollection.View<R> view) {
        views.put(key, view);
    }

    public void used(R element) throws IllegalArgumentException {
        if(!data.remove(element)) {
            throw new IllegalArgumentException(Objects.toString(element));
        }
    }

    public void usedAll(Collection<? extends R> elements) throws IllegalArgumentException {
        for(R e: elements) {
            used(e);
        }
    }

    public boolean isEmpty() {
        checkForChange();
        return views.isEmpty();
    }

    @Override
    public R drawValue() {
        if(isEmpty()) {
            throw new IllegalStateException("empty");
        }

        T key = mapping.getWeightedRandom(rnd);
        DataCollection.View<R> view = views.get(key);
        if(removeOnDraw) {
            return view.removeRandom(rnd);
        } else {
            return view.getRandom(rnd);
        }
    }
}
