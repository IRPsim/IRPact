package de.unileipzig.irpact.io.spec.impl.binary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class VisibleBinaryDataSpec
        implements ToSpecConverter<VisibleBinaryData>, ToParamConverter<VisibleBinaryData> {

    public static final VisibleBinaryDataSpec INSTANCE = new VisibleBinaryDataSpec();

    @Override
    public Class<VisibleBinaryData> getParamType() {
        return VisibleBinaryData.class;
    }

    @Override
    public void toSpec(VisibleBinaryData input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(manager.getBinaryData().get());
        SpecificationHelper list = spec.getArraySpec(TAG_list);
        create(input, list.addObject(), manager, converter, inline);
    }

    @Override
    public void create(VisibleBinaryData input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.set(TAG_binary, input.getName());
        spec.set(TAG_id, input.getID());
    }

    @Override
    public VisibleBinaryData[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(manager.getBinaryData().get());
        SpecificationHelper list = spec.getArraySpec(TAG_list);

        List<VisibleBinaryData> outList = new ArrayList<>();
        for(JsonNode root: list.iterateElements()) {
            SpecificationHelper entry = new SpecificationHelper(root);
            String bin = entry.getText(TAG_binary);
            long id = entry.getLong(TAG_id);
            VisibleBinaryData data = new VisibleBinaryData();
            data.setName(bin);
            data.setID(id);
            outList.add(data);
        }

        return outList.toArray(new VisibleBinaryData[0]);
    }
}
