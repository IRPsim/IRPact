package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.attribute.*;
import de.unileipzig.irpact.commons.locale.LocalizedData;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAdoptionAnalyser2 implements AdoptionAnalyser2 {

    protected static final String IF_NULL_STR = "-";
    protected static final double IF_NULL_DOUBLE = Double.NaN;
    protected static final String IF_NULL_DOUBLE_STR = Double.toString(IF_NULL_DOUBLE);

    protected VarCollectionWriter writer;
    protected VarCollection data;
    protected Collection<? extends Integer> years;
    protected int firstYear;
    protected LocalizedData localizedData;
    protected boolean skipInitial = true;

    public AbstractAdoptionAnalyser2() {
    }

    protected abstract VarCollection newCollection();

    public void setSkipInitial(boolean skipInitial) {
        this.skipInitial = skipInitial;
    }

    public boolean isSkipInitial() {
        return skipInitial;
    }

    public VarCollection getData() {
        if(data == null) {
            data = newCollection();
        }
        return data;
    }

    public void setYears(Collection<? extends Integer> years) {
        this.years = years;

        firstYear = Integer.MAX_VALUE;
        for(int year: years) {
            if(year < firstYear) {
                firstYear = year;
            }
        }
    }

    public void setWriter(VarCollectionWriter writer) {
        this.writer = writer;
    }

    protected abstract String getTypeForLocalization();

    protected abstract Function<? super Object[], ? extends Attribute[]> getMappingFunction();

    public void writeXlsx(XlsxVarCollectionWriter writer) throws IOException {
        VarCollectionWriter current = this.writer;
        setWriter(writer);
        if(localizedData != null) {
            WriterLocalizer.localizeXlsx(localizedData, writer, getTypeForLocalization());
        }
        writer.setEntry2Attribute(getMappingFunction());
        try {
            write();
        } finally {
            setWriter(current);
        }
    }

    protected abstract Function<? super Object[], ? extends String[]> getStringMappingFunction();

    public void writeCsv(CsvVarCollectionWriter writer) throws IOException {
        VarCollectionWriter current = this.writer;
        setWriter(writer);
        if(localizedData != null) {
            WriterLocalizer.localizeCsv(localizedData, writer, getTypeForLocalization());
        }
        writer.setEntry2Str(getStringMappingFunction());
        try {
            write();
        } finally {
            setWriter(current);
        }
    }

    public void setLocalizedData(LocalizedData localizedData) {
        this.localizedData = localizedData;
    }

    @Override
    public void write() throws IOException {
        if(writer == null) {
            throw new NullPointerException("missing writer");
        }

        writer.write(getData());
    }

    protected int getYearOrFirstYear(AdoptionEntry2 entry) {
        return entry.getTimestamp() == null
                ? firstYear
                : entry.getYear();
    }

    @Override
    public boolean add(AdoptionEntry2 entry) {
        if(skipInitial && entry.getAdoptedProduct().isInitial()) {
            return false;
        } else {
            int entryYear = getYearOrFirstYear(entry);
            for(Integer year: years) {
                if(year >= entryYear) {
                    add(year, entry);
                }
            }
            return true;
        }
    }

    protected abstract void add(int year, AdoptionEntry2 info);

    protected String findAgentStringAttributeValue(AdoptionEntry2 info, String attrName) {
        Attribute attr = info.getAgent().findAttribute(attrName);
        if(attr == null) {
            return null;
        }
        if(attr.isValueAttributeWithDataType(DataType.STRING)) {
            return attr.asValueAttribute().getStringValue();
        } else {
            return null;
        }
    }

    protected static String printPhase(AdoptionPhase phase, String ifNull) {
        return phase == null ? ifNull : phase.name();
    }

    protected static String printTime(ZonedDateTime time, String ifNull) {
        return time == null ? ifNull : TimeUtil.print(time);
    }

    protected static String print(Object obj, String ifNull) {
        return obj == null ? ifNull : Objects.toString(obj);
    }

    protected static String printValue(AdoptionResultInfo2 info, String ifNull) {
        return info == null ? ifNull : info.printValue();
    }

    protected static String printCumulativeValue(AdoptionResultInfo2 info, String ifNull) {
        return info == null ? ifNull : info.printCumulativeValue();
    }

    protected static long getSpatialInformationId(ConsumerAgent agent) {
        SpatialInformation information = agent.getSpatialInformation();
        if(information == null) {
            return -1;
        }
        if(information.hasId()) {
            return information.getId();
        } else {
            SpatialAttribute attr = information.getAttribute(RAConstants.ID);
            if(attr == null) {
                return -1;
            } else {
                return attr.asValueAttribute().getLongValue();
            }
        }
    }

    protected static String getZip(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.ZIP);
        if(attr == null || !attr.isValueAttributeWithDataType(DataType.STRING)) {
            return null;
        } else {
            return attr.asValueAttribute().getStringValue();
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected StringAttribute toStringAttribute(Object value, String ifNull) {
        return new BasicStringAttribute(value == null ? ifNull : Objects.toString(value));
    }

    @SuppressWarnings("SameParameterValue")
    protected DoubleAttribute toDoubleAttribute(Object value, double ifNull) {
        return new BasicDoubleAttribute(value == null ? ifNull : ((Number) value).doubleValue());
    }
}
