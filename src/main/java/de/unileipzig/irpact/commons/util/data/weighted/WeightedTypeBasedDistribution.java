package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.Distribution;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCollection;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class WeightedTypeBasedDistribution<T, U> extends NameableBase implements Distribution<U> {

    protected DataCollection<U> data;
    protected Map<T, DataCollection.View<U>> views;
    protected WeightedMapping<T> mapping;
    protected Rnd rnd;
    protected boolean removeOnDraw = false;

    public WeightedTypeBasedDistribution() {
        this(new NavigableMapWeightedMapping<>(), new LinkedHashMap<>());
    }

    public WeightedTypeBasedDistribution(WeightedMapping<T> mapping, Map<T, DataCollection.View<U>> views) {
        this.mapping = mapping;
        this.views = views;
    }

    protected void checkForChange() {
        List<T> toRemove = new ArrayList<>();
        for(Map.Entry<T, DataCollection.View<U>> entry: views.entrySet()) {
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

    public void setData(DataCollection<U> data) {
        this.data = data;
    }

    public DataCollection<U> getData() {
        return data;
    }

    public void set(T key, DataCollection.Filter<? super U> filter) {
        if(views.containsKey(key)) {
            throw new IllegalArgumentException("key already exists: " + key);
        }

        DataCollection.View<U> view = data.createView(filter);

        views.put(key, view);
        mapping.set(key, view.size());
    }

    public void used(U element) throws IllegalArgumentException {
        if(!data.remove(element)) {
            throw new IllegalArgumentException(Objects.toString(element));
        }
    }

    public void usedAll(Collection<? extends U> elements) throws IllegalArgumentException {
        for(U e: elements) {
            used(e);
        }
    }

    public boolean isEmpty() {
        checkForChange();
        return views.isEmpty();
    }

    @Override
    public U drawValue() {
        if(isEmpty()) {
            throw new IllegalStateException("empty");
        }

        T key = mapping.getWeightedRandom(rnd);
        DataCollection.View<U> view = views.get(key);
        if(removeOnDraw) {
            return view.removeRandom(rnd);
        } else {
            return view.getRandom(rnd);
        }
    }
}
