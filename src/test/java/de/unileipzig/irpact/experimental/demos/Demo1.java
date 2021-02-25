package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.param.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import org.junit.jupiter.api.Disabled;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo1 implements DefaultScenarioFactory {

    private static InRoot getRoot() {
        throw new TodoException();
//        Map<String, Object> cache = new HashMap<>();
//        cache.put("DiraqDistribution1", new InConstantUnivariateDistribution("DiraqDistribution1", 1));
//        cache.put("DiraqDistribution0", new InConstantUnivariateDistribution("DiraqDistribution0", 0));
//
//        InConsumerAgentGroup A = DemoUtil.createGroup(
//                "A",
//                "DiraqDistribution1",
//                cache
//        );
//        InConsumerAgentGroup B = DemoUtil.createGroup(
//                "B",
//                "DiraqDistribution0",
//                cache
//        );
//
//        InCustomSpatialDistribution2D A_spa = new InCustomSpatialDistribution2D(
//                "A_spatial",
//                A,
//                0, 0
//        );
//        InCustomSpatialDistribution2D B_spa = new InCustomSpatialDistribution2D(
//                "B_spatial",
//                B,
//                0, 0
//        );
//
//        InRoot root = new InRoot();
//        root.general.seed = 123;
//        root.general.timeout = TimeUnit.MINUTES.toMillis(5);
//        root.general.startYear = 2015;
//        root.general.endYear = 2015;
//        root.version = new InVersion[]{InVersion.currentVersion()};
//
//        root.consumerAgentGroups = new InConsumerAgentGroup[]{A, B};
//
//        root.graphTopologySchemes = DemoUtil.getGraphTopo();
//
//        root.processModel = new InProcessModel[]{
//                new InRAProcessModel("", 0.25, 0.25, 0.25, 0.25, 3, 2, 1, 0)
//        };
//        InRandomBoundedIntegerDistribution b_0_91 = new InRandomBoundedIntegerDistribution("b_0_91", 0, 91);
//        root.orientationSupplier = new InOrientationSupplier[]{
//                new InOrientationSupplier("ori_supplier", b_0_91)
//        };
//        root.slopeSupplier = new InSlopeSupplier[]{
//                new InSlopeSupplier("slope_supplier", b_0_91)
//        };
//
//        root.productGroups = DemoUtil.createProd(cache);
//
//        root.spatialModel = new InSpatialModel[]{new InSpace2D("Space2D", true)};
//        root.spatialDistributions = new InSpatialDistribution[]{A_spa, B_spa};
//
//        root.timeModel = DemoUtil.getDiscrete1D();
//
//        return root;
    }

    @Override
    public InRoot createDefaultScenario() {
        return getRoot();
    }
}
