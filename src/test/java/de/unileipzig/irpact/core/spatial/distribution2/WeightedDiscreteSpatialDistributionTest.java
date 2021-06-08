package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.spatial.*;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.xxx.IRPactMock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class WeightedDiscreteSpatialDistributionTest {

    @Disabled
    @Test
    void testMockIds() {
        Supplier<SpatialInformation> supplier1 = IRPactMock.mockSpatialInformation2D(new Rnd(123));
        Supplier<SpatialInformation> supplier2 = IRPactMock.mockSpatialInformation2D(new Rnd(123));
        for(int i = 0; i < 10; i++) {
            assertTrue(supplier1.get().isEquals(supplier2.get()), "error @ i = " + i);
        }
    }

    @Test
    void testFilters() {
        Map<String, SpatialDataFilter> filters = SpatialUtil.createFilters(
                "milieu", Collections.singletonList("a"),
                "zip", Arrays.asList("x", "y", "z")
        );
        System.out.println(filters.keySet());
    }

    @Test
    void testIt() {
        Function<Rnd, Collection<SpatialAttribute>> milieusAndZip = IRPactMock.mockTwoStringAttributes(
                "milieu", Collections.singletonList("a"),
                "zip", Arrays.asList("x", "x", "x", "y", "z")
        );
        Supplier<SpatialInformation> supplier = IRPactMock.mockSpatialInformation2D(new Rnd(123), milieusAndZip);
        SpatialDataCollection dataColl = IRPactMock.mockSpatialDataCollection(supplier, 20);
        Map<String, SpatialDataFilter> filters = SpatialUtil.createFilters("milieu", Collections.singletonList("a"), "zip", Arrays.asList("x", "y", "z"));
        filters.values().forEach(f -> System.out.println(f.getName()));
        assertEquals(3, filters.size());
        WeightedDiscreteSpatialDistribution dist = new WeightedDiscreteSpatialDistribution();
        dist.setSpatialData(dataColl);
        dist.addFilters(filters.values());
        dist.rebuildWeightedMapping(true);
    }
}