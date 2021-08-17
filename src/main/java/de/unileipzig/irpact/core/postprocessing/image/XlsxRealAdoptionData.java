package de.unileipzig.irpact.core.postprocessing.image;

import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class XlsxRealAdoptionData implements RealAdoptionData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxRealAdoptionData.class);

    protected TypedMatrix<String, String, Integer> data;

    public XlsxRealAdoptionData() {
    }

    public XlsxRealAdoptionData(TypedMatrix<String, String, Integer> data) {
        setData(data);
    }

    public void setData(TypedMatrix<String, String, Integer> data) {
        this.data = data;
    }

    public TypedMatrix<String, String, Integer> getData() {
        return data;
    }

    @Override
    public int get(int year, String zip) {
        if(data == null) {
            throw new NullPointerException("data");
        }
        int adoptions = data.getOrDefault(zip, Integer.toString(year), -1);
        if(adoptions == -1) {
            LOGGER.warn("missing adoption data for zip '{}' and year '{}'", zip, year);
            return 0;
        } else {
            return adoptions;
        }
    }
}