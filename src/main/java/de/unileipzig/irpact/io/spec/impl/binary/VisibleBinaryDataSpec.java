package de.unileipzig.irpact.io.spec.impl.binary;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class VisibleBinaryDataSpec extends SpecBase<VisibleBinaryData, VisibleBinaryData[]> {

    @Override
    public VisibleBinaryData[] toParam(SpecificationManager manager, Map<String, Object> cache) {
        List<VisibleBinaryData> list = new ArrayList<>();

        SpecificationHelper rootSpec = new SpecificationHelper(manager.getBinaryDataRoot());
        SpecificationHelper content = rootSpec.getArraySpec(TAG_content);

        for(JsonNode child: content.iterateElements()) {
            SpecificationHelper childSpec = new SpecificationHelper(child);
            String binary = childSpec.getText(TAG_binary);
            long id = childSpec.getLong(TAG_id);

            VisibleBinaryData vbd = new VisibleBinaryData();
            vbd.setName(binary);
            vbd.setID(id);
            list.add(vbd);
        }
        return list.toArray(new VisibleBinaryData[0]);
    }

    @Override
    public Class<VisibleBinaryData> getParamType() {
        return VisibleBinaryData.class;
    }

    @Override
    public void toSpec(VisibleBinaryData instance, SpecificationManager manager, SpecificationConverter converter) {
        SpecificationHelper spec = new SpecificationHelper(manager.getBinaryDataRoot());
        SpecificationHelper content = spec.getArraySpec(TAG_content);
        SpecificationHelper newEntry = content.addObjectSpec();
        newEntry.set(TAG_binary, instance.getName());
        newEntry.set(TAG_id, instance.getID());
    }
}
