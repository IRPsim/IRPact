package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public class PVactDataBuilder {

    protected final Map<String, Integer> SIZES = new LinkedHashMap<>();
    protected final Map<String, RowHandler> INITALIZERS = new LinkedHashMap<>();

    public PVactDataBuilder() {
    }

    public PVactDataBuilder(
            String[] names,
            int[] sizes,
            RowHandler[] initalizers) {
        init(names, sizes, initalizers);
    }

    public void init(
            String[] names,
            int[] sizes,
            RowHandler[] initalizers) {
        setSizes(names, sizes);
        setInitializers(names, initalizers);
    }

    public void setSizes(String[] cagNames, int[] sizes) {
        SIZES.clear();
        if(cagNames.length != sizes.length) {
            throw new IllegalArgumentException("size mismatch");
        }
        for(int i = 0; i < cagNames.length; i++) {
            setSize(cagNames[i], sizes[i]);
        }
    }

    public void setSize(String cagName, int size) {
        SIZES.put(cagName, size);
    }

    public int getSize(String cagName) {
        return SIZES.get(cagName);
    }

    public void setInitializers(String[] cagNames, RowHandler[] initalizers) {
        INITALIZERS.clear();
        if(cagNames.length != initalizers.length) {
            throw new IllegalArgumentException("size mismatch");
        }
        for(int i = 0; i < cagNames.length; i++) {
            setInitializer(cagNames[i], initalizers[i]);
        }
    }

    public void setInitializer(String cagName, RowHandler initalizer) {
        INITALIZERS.put(cagName, initalizer);
    }

    public Consumer<? super List<SpatialAttribute>> getInitalizer(String cagName) {
        return INITALIZERS.get(cagName);
    }

    public int getTotalSize() {
        return SIZES.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public List<List<SpatialAttribute>> createTestData(List<List<SpatialAttribute>> input, Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, getTotalSize(), rnd);
        int from = 0;
        int to = 0;

        for(String cagName: SIZES.keySet()) {
            to += getSize(cagName);
            for(int i = from; i < to; i++) {
                List<SpatialAttribute> row = output.get(i);
                SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, cagName);
                Consumer<? super List<SpatialAttribute>> initalizer = getInitalizer(cagName);
                if(initalizer != null) {
                    initalizer.accept(row);
                }
            }
            from += getSize(cagName);
        }

        return output;
    }

    public boolean isEmpty() {
        return SIZES.isEmpty();
    }

    /**
     * @author Daniel Abitz
     */
    public interface RowHandler extends Consumer<List<SpatialAttribute>> {
    }
}
