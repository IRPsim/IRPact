package de.unileipzig.irpact.misc;

import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.io.base.AnnualEntry;

/**
 * @author Daniel Abitz
 */
@Todo("FIX")
public final class ExampleFactory {

    public static AnnualEntry<InRoot> buildMinimalExampleWith2Groups() {
        if(true) throw new TodoException();

        InRoot root = new InRoot();
        root.general = ExampleUtil.buildGeneral();
        root.consumerAgentGroups = new InConsumerAgentGroup[]{
                ExampleUtil.buildRelativeAgreementCag("A", 1),
                ExampleUtil.buildRelativeAgreementCag("B", 1)
        };
        root.affinityEntries = ExampleUtil.buildAffinityEntries(root.consumerAgentGroups);
        root.graphTopologySchemes = ExampleUtil.getCompleteGraph();
        root.processModel = ExampleUtil.buildRelativeAgreementModel();
//        root.slopeSupplier = ExampleUtil.getDefaultSlope();
//        root.orientationSupplier = ExampleUtil.getDefaultOrientation();
        root.productGroups = new InProductGroup[]{ExampleUtil.buildPV()};
        root.spatialModel = ExampleUtil.space2D();
//        root.spatialDistributions = ExampleUtil.buildSpatialDists(root.consumerAgentGroups);
        root.timeModel = ExampleUtil.buildDiscreteMonth();
        return ExampleUtil.buildEntry(root);
    }
}
