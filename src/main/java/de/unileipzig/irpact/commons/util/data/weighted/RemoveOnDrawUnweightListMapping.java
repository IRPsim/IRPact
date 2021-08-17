package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class RemoveOnDrawUnweightListMapping<T> extends UnweightListMapping<T> {

    protected boolean removeFrist = false;

    public RemoveOnDrawUnweightListMapping() {
        this(LinkedList::new);
    }

    public RemoveOnDrawUnweightListMapping(Supplier<? extends List<T>> listSupplier) {
        super(listSupplier);
    }

    public void setRemoveFrist(boolean removeFrist) {
        this.removeFrist = removeFrist;
    }

    public boolean isRemoveFrist() {
        return removeFrist;
    }

    public boolean getAll(Rnd rnd, Collection<? super T> out) {
        boolean changed = false;
        while(values.size() > 0) {
            T draw = getRandom(rnd);
            changed |= out.add(draw);
        }
        return changed;
    }

    @Override
    public T getRandom(Rnd rnd) {
        requiresNotEmpty();
        if(removeFrist) {
            return values.remove(0);
        } else {
            return rnd.removeRandom(values);
        }
    }
}
