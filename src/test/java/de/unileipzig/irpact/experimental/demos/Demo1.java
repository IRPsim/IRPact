package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.input.InExample;
import de.unileipzig.irpact.io.input.InRoot;
import de.unileipzig.irpact.io.input.InVersion;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.input.process.InProcessModel;
import de.unileipzig.irpact.io.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.input.spatial.InConstantSpatialDistribution2D;
import de.unileipzig.irpact.io.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.input.spatial.InSpatialDistribution;
import de.unileipzig.irpact.io.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.output.OutRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo1 implements DefaultScenarioFactory {

    private static InRoot getRoot() {
        Map<String, Object> cache = new HashMap<>();
        cache.put("DiraqDistribution1", new InConstantUnivariateDistribution("DiraqDistribution1", 1));
        cache.put("DiraqDistribution0", new InConstantUnivariateDistribution("DiraqDistribution0", 0));

        InConsumerAgentGroup A = DemoUtil.createGroup(
                "A",
                "DiraqDistribution1",
                cache
        );
        InConsumerAgentGroup B = DemoUtil.createGroup(
                "B",
                "DiraqDistribution0",
                cache
        );

        InConstantSpatialDistribution2D A_spa = new InConstantSpatialDistribution2D(
                "A_spatial",
                A,
                0, 0
        );
        InConstantSpatialDistribution2D B_spa = new InConstantSpatialDistribution2D(
                "B_spatial",
                B,
                0, 0
        );

        InRoot root = new InRoot();
        root.general.seed = 123;
        root.general.timeout = TimeUnit.MINUTES.toMillis(5);
        root.general.startYear = 2015;
        root.general.endYear = 2015;
        root.version = new InVersion[]{InVersion.currentVersion()};

        root.consumerAgentGroups = new InConsumerAgentGroup[]{A, B};

        root.graphTopologySchemes = DemoUtil.getGraphTopo();

        root.processModel = new InProcessModel[]{
                new InRAProcessModel("", 0.25, 0.25, 0.25, 0.25, 3, 2, 1, 0)
        };
        InRandomBoundedIntegerDistribution b_0_91 = new InRandomBoundedIntegerDistribution("b_0_91", 0, 91);
        root.orientationSupplier = new InOrientationSupplier[]{
                new InOrientationSupplier("ori_supplier", b_0_91)
        };
        root.slopeSupplier = new InSlopeSupplier[]{
                new InSlopeSupplier("slope_supplier", b_0_91)
        };

        root.productGroups = DemoUtil.createProd(cache);

        root.spatialModel = new InSpatialModel[]{new InSpace2D("Space2D", true)};
        root.spatialDistributions = new InSpatialDistribution[]{A_spa, B_spa};

        root.timeModel = DemoUtil.getDiscrete1D();

        return root;
    }

    @Override
    public InRoot createDefaultScenario() {
        return getRoot();
    }
}
