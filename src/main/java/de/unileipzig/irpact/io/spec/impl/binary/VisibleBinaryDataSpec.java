package de.unileipzig.irpact.io.spec.impl.binary;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.spec.BidirectionalConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class VisibleBinaryDataSpec implements BidirectionalConverter<VisibleBinaryData> {

    public static final VisibleBinaryDataSpec INSTANCE = new VisibleBinaryDataSpec();

    @Override
    public VisibleBinaryData[] toParamArray(SpecificationJob job) throws ParsingException {
        SpecificationHelper root = SpecificationHelper.of(job.getData().getBinaryData().get());
        SpecificationHelper list = root.getOrCreateArray(TAG_list);

        List<VisibleBinaryData> dataList = new ArrayList<>();
        for(SpecificationHelper child: list.iterateElements()) {
            VisibleBinaryData data = toParam(child.rootAsObject(), job);
            dataList.add(data);
        }
        return dataList.toArray(new VisibleBinaryData[0]);
    }

    @Override
    public VisibleBinaryData toParam(ObjectNode root, SpecificationJob job) throws ParsingException {
        SpecificationHelper entry = SpecificationHelper.of(root);
        String content = entry.getText(TAG_binary);
        long id = entry.getLong(TAG_id);
        return new VisibleBinaryData(content, id);
    }

    @Override
    public Class<VisibleBinaryData> getParamType() {
        return VisibleBinaryData.class;
    }

    @Override
    public void toSpec(VisibleBinaryData input, SpecificationJob job) throws ParsingException {
        SpecificationHelper spec = SpecificationHelper.of(job.getData().getBinaryData().get());
        SpecificationHelper list = spec.getOrCreateArray(TAG_list);
        create(input, list.addObjectNode(), job);
    }

    @Override
    public void create(VisibleBinaryData input, ObjectNode root, SpecificationJob job) throws ParsingException {
        SpecificationHelper spec = SpecificationHelper.of(root);
        spec.set(TAG_binary, input.getName());
        spec.set(TAG_id, input.getID());
    }
}
