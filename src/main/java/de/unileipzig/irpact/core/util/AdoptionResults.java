package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.time.Timestamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class AdoptionResults implements Iterable<AdoptionResult> {

    protected List<AdoptionResult> list = new ArrayList<>();

    public AdoptionResults() {
    }

    public void add(AdoptionResult result) {
        list.add(result);
    }

    public List<AdoptionResult> filterYear(int year) {
        return filter(result -> {
            Timestamp timestamp = result.getProduct().getTimestamp();
            return timestamp.getYear() == year;
        });
    }

    public List<AdoptionResult> filter(Predicate<? super AdoptionResult> filter) {
        return list.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<AdoptionResult> iterator() {
        return list.iterator();
    }
}
