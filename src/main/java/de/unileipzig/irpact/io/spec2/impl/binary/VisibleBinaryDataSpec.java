package de.unileipzig.irpact.io.spec2.impl.binary;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.spec2.BidirectionalConverter2;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class VisibleBinaryDataSpec implements BidirectionalConverter2<VisibleBinaryData> {

    public static final VisibleBinaryDataSpec INSTANCE = new VisibleBinaryDataSpec();

    @Override
    public VisibleBinaryData[] toParamArray(SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 root = SpecificationHelper2.of(job.getData().getBinaryData().get());
        SpecificationHelper2 list = root.getOrCreateArray(TAG_list);

        List<VisibleBinaryData> dataList = new ArrayList<>();
        for(SpecificationHelper2 child: list.iterateElements()) {
            VisibleBinaryData data = toParam(child.rootAsObject(), job);
            dataList.add(data);
        }
        return dataList.toArray(new VisibleBinaryData[0]);
    }

    @Override
    public VisibleBinaryData toParam(ObjectNode root, SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 entry = SpecificationHelper2.of(root);
        String content = entry.getText(TAG_binary);
        long id = entry.getLong(TAG_id);
        return new VisibleBinaryData(content, id);
    }

    @Override
    public Class<VisibleBinaryData> getParamType() {
        return VisibleBinaryData.class;
    }

    @Override
    public void toSpec(VisibleBinaryData input, SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 spec = SpecificationHelper2.of(job.getData().getBinaryData().get());
        SpecificationHelper2 list = spec.getOrCreateArray(TAG_list);
        create(input, list.addObjectNode(), job);
    }

    @Override
    public void create(VisibleBinaryData input, ObjectNode root, SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 spec = SpecificationHelper2.of(root);
        spec.set(TAG_binary, input.getName());
        spec.set(TAG_id, input.getID());
    }
}
