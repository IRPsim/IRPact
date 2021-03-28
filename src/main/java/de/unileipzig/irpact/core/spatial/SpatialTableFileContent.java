package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.data.FileContent;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileContent implements FileContent {

    protected List<List<SpatialAttribute>> data;

    public SpatialTableFileContent(List<List<SpatialAttribute>> data) {
        this.data = data;
    }

    @Override
    public List<List<SpatialAttribute>> data() {
        return data;
    }

    public List<SpatialAttribute> get(int index) {
        return data.get(index);
    }

    public int size() {
        return data.size();
    }
}
