package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualAdoptionsX1<A> extends AbstractAdoptionAnalyser2 {

    public AbstractAnnualAdoptionsX1() {
    }

    protected abstract Class<A> getFirstType();

    public abstract String getTypeForLocalization();

    @Override
    protected VarCollection newCollection() {
        return new VarCollection(Integer.class, getFirstType(), AdoptionResultInfo2.class);
    }

    @Override
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

    @Override
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
}
