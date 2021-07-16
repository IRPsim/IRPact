package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.attribute.*;
import de.unileipzig.irpact.commons.locale.LocalizedData;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAdoptionAnalyser2 implements AdoptionAnalyser2 {

    protected VarCollectionWriter writer;
    protected VarCollection data;
    protected Collection<? extends Integer> years;
    protected LocalizedData localizedData;

    public AbstractAdoptionAnalyser2() {
    }

    protected abstract VarCollection newCollection();

    public VarCollection getData() {
        if(data == null) {
            data = newCollection();
        }
        return data;
    }

    public void setYears(Collection<? extends Integer> years) {
        this.years = years;
    }

    public void setWriter(VarCollectionWriter writer) {
        this.writer = writer;
    }

    protected abstract Function<? super Object[], ? extends Attribute[]> getMappingFunction();

    public abstract void writeXlsx(XlsxVarCollectionWriter writer) throws IOException;

    protected abstract Function<? super Object[], ? extends String[]> getStringMappingFunction();

    public abstract void writeCsv(CsvVarCollectionWriter writer) throws IOException;

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

    @Override
    public boolean add(AdoptionEntry2 entry) {
        if(entry.getAdoptedProduct().isInitial()) {
            return false;
        } else {
            for(Integer year: years) {
                if(year >= entry.getYear()) {
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

    protected StringAttribute toStringAttribute(Object value) {
        return new BasicStringAttribute((String) value);
    }

    protected DoubleAttribute toDoubleAttribute(Object value) {
        return new BasicDoubleAttribute(((Number) value).doubleValue());
    }
}
