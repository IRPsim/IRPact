package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopologySpec extends SpecBase<InUnlinkedGraphTopology, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InUnlinkedGraphTopology> getParamType() {
        return InUnlinkedGraphTopology.class;
    }

    @Override
    public void toSpec(InUnlinkedGraphTopology instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getTopology());
        spec.setName(instance.getName());
        spec.setType("UnlinkedGraphTopology");
    }
}
