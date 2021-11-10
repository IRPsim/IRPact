package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Point2D;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public class DataCreator {

    public DataCreator() {
    }

    protected static List<List<SpatialAttribute>> drawRandom(List<List<SpatialAttribute>> input, int count, Random rnd) {
        List<List<SpatialAttribute>> output = new ArrayList<>(input);
        Collections.shuffle(output, rnd);
        if(count < output.size()) {
            output.subList(count, output.size()).clear();
        }
        return output;
    }

    public static List<List<SpatialAttribute>> modify(DataSetup setup, List<List<SpatialAttribute>> input) {
        List<List<SpatialAttribute>> output = new ArrayList<>(input);

        for(int i = 0; i < input.size(); i++) {
            List<SpatialAttribute> modified = input.get(i);
            if(setup.hasGlobalModifier()) {
                modified = setup.getGlobalModifier().modify(modified);
            }
            input.set(i, modified);
        }

        return output;
    }

    public static List<List<SpatialAttribute>> apply(DataSetup setup, List<List<SpatialAttribute>> input, Random rnd) {
        List<List<SpatialAttribute>> output = drawRandom(input, setup.getTotalSize(), rnd);

        int from = 0;
        int to = 0;

        for(String cagName: setup.getCagGroups()) {

            to += setup.getSize(cagName);

            for(int i = from; i < to; i++) {
                List<SpatialAttribute> row = output.get(i);

                DataSetup.setCagGroup(row, cagName);
                DataSetup.addOriginalIdAttribute(row);
                DataSetup.setId(row, i);

                List<SpatialAttribute> modified = row;
                if(setup.hasGlobalModifier()) {
                    modified = setup.getGlobalModifier().modify(modified);
                }
                if(setup.hasModifier(cagName)) {
                    modified = setup.getModifier(cagName).modify(modified);
                }
                output.set(i, modified);
            }

            from += setup.getSize(cagName);
        }

        return output;
    }

    public static List<List<SpatialAttribute>> extractRandomEntries(
            List<List<SpatialAttribute>> input,
            int count,
            Predicate<? super List<SpatialAttribute>> filter,
            Random rnd) {
        List<List<SpatialAttribute>> output = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            output.add(extractRandomEntry(input, filter, rnd));
        }
        return output;
    }

    public static List<SpatialAttribute> extractRandomEntry(
            List<List<SpatialAttribute>> input,
            Predicate<? super List<SpatialAttribute>> filter,
            Random rnd) {
        Set<Integer> indicies = new HashSet<>();
        while(true) {
            int rndIndex;

            while(true) {
                rndIndex = rnd.nextInt(input.size());
                if(indicies.add(rndIndex)) {
                    break;
                }
                if(indicies.size() == input.size()) {
                    throw new IllegalStateException("no data left");
                }
            }

            List<SpatialAttribute> rndEntry = input.get(rndIndex);
            if(filter.test(rndEntry)) {
                input.remove(rndIndex);
                return rndEntry;
            }
        }
    }

    public static Predicate<List<SpatialAttribute>> largerDistance(
            List<SpatialAttribute> origin,
            int distance,
            Metric2D metric) {
        Point2D originPoint = toPoint2D(origin);
        return other -> {
            double dist = metric.distance(originPoint, toPoint2D(other));
            return dist > distance;
        };
    }

    public static Predicate<List<SpatialAttribute>> smallerDistance(
            List<SpatialAttribute> origin,
            int distance,
            Metric2D metric) {
        Point2D originPoint = toPoint2D(origin);
        return other -> {
            double dist = metric.distance(originPoint, toPoint2D(other));
            return dist < distance;
        };
    }

    protected static Point2D toPoint2D(List<SpatialAttribute> attr) {
        return new BasicPoint2D(
                SpatialUtil.secureGet(attr, RAConstants.X_CENT).getDoubleValue(),
                SpatialUtil.secureGet(attr, RAConstants.Y_CENT).getDoubleValue()
        );
    }
}
