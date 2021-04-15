package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.util.PVactUtil;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.AnnualEntry;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModelUtil {

    public static final Path dataDir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");

    public static final List<String> MILIEUS = PVactUtil.MILIEUS;

    public static Path getData(String name) {
        return dataDir.resolve(name);
    }

    public static List<SpatialInformation> getInformations(String name) throws IOException {
        Path path = getData(name);
        List<List<SpatialAttribute>> attrs = SpatialTableFileLoader.parseXlsx(path);
        return SpatialUtil.mapToPoint2D(attrs, RAConstants.X_CENT, RAConstants.Y_CENT);
    }

    public static AnnualData<InRoot> buildData(InRoot root) {
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.general.firstSimulationYear);
        return data;
    }

    public static AnnualEntry<InRoot> buildEntry(InRoot root) {
        return buildData(root).get();
    }

    public static void run(String modelName, InRoot root) {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x9");
        run(dir, modelName, root);
    }

    public static void run(Path dir, String modelName, InRoot root) {
        String[] args = {
                "--testMode",
                "-o", dir.resolve("scenariosX").resolve(modelName + "-output.json").toString(),
                "--logPath", dir.resolve("scenariosX").resolve(modelName + "-log.log").toString(),
                "--logConsoleAndFile",
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };

        AnnualEntry<InRoot> entry = buildEntry(root);
        Start.start(args, entry);
    }
}
