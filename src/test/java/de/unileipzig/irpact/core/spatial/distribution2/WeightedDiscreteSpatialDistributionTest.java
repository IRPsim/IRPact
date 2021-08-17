package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.spatial.*;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.develop.Todo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Todo("richtig implementieren")
class WeightedDiscreteSpatialDistributionTest {

//    @Disabled
//    @Test
//    void testMockIds() {
//        Supplier<SpatialInformation> supplier1 = IRPactMock.mockSpatialInformation2D(new Rnd(123));
//        Supplier<SpatialInformation> supplier2 = IRPactMock.mockSpatialInformation2D(new Rnd(123));
//        for(int i = 0; i < 10; i++) {
//            assertTrue(supplier1.get().isEquals(supplier2.get()), "error @ i = " + i);
//        }
//    }
//
//    @Disabled
//    @Test
//    void testFilters() {
//        List<SpatialDataFilter> filters = SpatialUtil.createFilters(
//                "milieu", Collections.singletonList("a"),
//                "zip", Arrays.asList("x", "y", "z")
//        );
//        filters.forEach(f -> System.out.println(f.getName()));
//    }
//
//    @Test
//    void testIt() {
//        Function<Rnd, Collection<SpatialAttribute>> milieusAndZip = IRPactMock.mockTwoStringAttributes(
//                "milieu", Collections.singletonList("a"),
//                "zip", Arrays.asList("x", "x", "x", "y", "z")
//        );
//        Supplier<SpatialInformation> supplier = IRPactMock.mockSpatialInformation2D(new Rnd(123), milieusAndZip);
//        SpatialDataCollection dataColl = IRPactMock.mockSpatialDataCollection(supplier, 20);
//        List<SpatialDataFilter> filters = SpatialUtil.createFilters("milieu", Collections.singletonList("a"), "zip", Arrays.asList("x", "y", "z"));
//        filters.forEach(f -> System.out.println(f.getName()));
//        assertEquals(3, filters.size());
//        WeightedDiscreteSpatialDistribution dist = new WeightedDiscreteSpatialDistribution();
//        dist.setSpatialData(dataColl);
//        dist.addFilters(filters);
//        dist.rebuildWeightedMapping(true);
//    }
}