package de.unileipzig.irpact.experimental.demonstration;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.res.BasicResourceLoader;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomFileSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
public class SelectAndGroupDemo {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SelectAndGroupDemo.class);

    /*
     * - nerviges Format in .xlsx -> siehe SpatialTableFileLoader.extractRowData
     * - Zeichensatzfehler in Daten -> z.B. Zeile 646
     */
    @Test
    void testIt() throws MissingDataException {
        IRPLogging.initConsole();

        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setDir(dir);

        SpatialTableFileLoader l = new SpatialTableFileLoader();
        l.setLoader(loader);
        l.setInputFileName("GIS_final_1_x");
        l.initalize();

        List<List<SpatialAttribute<?>>> allEntries = l.getAllAttributes();
        LOGGER.info("Entraege: {}", allEntries.size());
        LOGGER.info("erster Eintrag: {}", allEntries.get(0));

        WeightedDiscreteSpatialDistribution dist = InCustomFileSelectedGroupedSpatialDistribution2D.createInstance(
                "TestDist",
                allEntries,
                new ConstantUnivariateDoubleDistribution("x", 0),
                new ConstantUnivariateDoubleDistribution("y", 0),

//                "Mic_Dominantes_Milieu",          //!!!
//                "TRA",                          //!!! Name der Cag
                "FS_Eigentum",
                "Stadt Leipzig",

                "Dummy_PLZ",                   //!!!
                new Rnd(42)
        );

        LOGGER.info("Anzahl nach Filterung: {}", dist.countUnusedEntries());
        LOGGER.info("Nicht normalisiert: {}", dist.getWeightedMapping().getMapping());
        LOGGER.info("Normalisiert und sortiert: {}", dist.getUnmodifiableWeightedMapping().getNormalizedSortedValues());

        System.out.println("==========");
        SpatialInformation info = dist.drawValue();
        LOGGER.info("Info#0: {}", info);
        LOGGER.info("Info#0 Attribute: {}", info.getAttributes());

        //alle a rausziehen
        int aSize = dist.getUnused().get("a").size();
        for(int i = 0; i < aSize; i++) {
            dist.drawValue("a");
        }

        System.out.println("==========");
        LOGGER.info("noch ungenutzt: {}", dist.countUnusedEntries());
        LOGGER.info("genutzte: {}", dist.countUsedEntries());
        LOGGER.info("Nicht normalisiert: {}", dist.getWeightedMapping().getMapping());
        LOGGER.info("Normalisiert und sortiert: {}", dist.getUnmodifiableWeightedMapping().getNormalizedSortedValues());

        System.out.println("==========");
        int rest = dist.countUnusedEntries();
        for(int i = 0; i < rest; i++) {
            dist.drawValue();
        }
        try {
            dist.drawValue();
        } catch (IllegalStateException e) {
            LOGGER.info("LEER");
        }
    }
}
