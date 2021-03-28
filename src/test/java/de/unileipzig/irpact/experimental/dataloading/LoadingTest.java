package de.unileipzig.irpact.experimental.dataloading;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.res.BasicResourceLoader;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomFileSelectedGroupedSpatialDistribution2D;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Disabled
public class LoadingTest {

    @Test
    void testStuff() {
        IRPLogging.initConsole();

        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setDir(dir);

        String file0 = "BarwertrechnerMini_ES.xlsx";
        String file1 = "GIS_final_1.xlsx";
        String file2 = "x.txt";

        assertTrue(loader.exists(file0));
        assertTrue(loader.exists(file1));
        assertTrue(loader.exists(file2));

        assertTrue(loader.hasPath(file0));
        assertTrue(loader.hasPath(file1));
        assertFalse(loader.hasPath(file2));

        assertTrue(loader.hasResource(file0));
        assertTrue(loader.hasResource(file1));
        assertTrue(loader.hasResource(file2));
    }

    @Test
    void testLoadXlsx() throws MissingDataException {
        IRPLogging.initConsole();

        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setDir(dir);

        SpatialTableFileLoader l = new SpatialTableFileLoader();
        l.setLoader(loader);
        l.setInputFileName("GIS_final_1_x");
        l.initalize();
        List<List<SpatialAttribute<?>>> attrs = l.getAllAttributes();
        System.out.println(attrs.size());
        System.out.println(attrs.get(0));

        WeightedDiscreteSpatialDistribution dist = InCustomFileSelectedGroupedSpatialDistribution2D.createInstance(
                "TEST",
                attrs,
                new ConstantUnivariateDoubleDistribution("x", 0.0),
                new ConstantUnivariateDoubleDistribution("y", 0.0),
                "Mic_Dominantes_Milieu",
                "TRA",
                "Dummy_PLZ",
                new Rnd(42)
        );
        System.out.println(dist.getWeightedMapping().getMapping());
        System.out.println(dist.getUnmodifiableWeightedMapping().getNormalizedSortedValues());

        System.out.println(dist.getUnused().get("a").size());

//        for(int i = 0; i < 617; i++) {
//            dist.drawValue("a");
//        }
//        System.out.println("null? " + (dist.getUnused().get("a") == null));
//        System.out.println(dist.getUsed().get("a").size());
//
//        System.out.println(dist.getWeightedMapping().getMapping());
//        System.out.println(dist.getUnmodifiableWeightedMapping().getNormalizedSortedValues());

        for(int i = 0; i < 617; i++) {
            dist.drawValue("a");
        }
        for(int i = 0; i < 889; i++) {
            dist.drawValue("b");
        }
        for(int i = 0; i < 1890; i++) {
            dist.drawValue("c");
        }
        System.out.println(dist.getWeightedMapping().getMapping());
        System.out.println(dist.getUnmodifiableWeightedMapping().getNormalizedSortedValues());
        dist.drawValue();
    }
}
