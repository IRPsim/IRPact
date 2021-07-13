package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.commons.util.xlsx.StandardCellValueConverter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetWriter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class XlsxVarCollectionWriter implements VarCollectionWriter {

    public static final Attribute NA_STR = new BasicStringAttribute("-");

    protected Function<? super Object[], ? extends Attribute[]> entry2attribute;
    protected Path target;
    protected XlsxTable<Attribute> table;
    protected List<String> infos = new ArrayList<>();
    protected String sheetName = "output";

    public XlsxVarCollectionWriter() {
    }

    public void setTarget(Path target) {
        this.target = target;
    }

    public Path getTarget() {
        return target;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void addInfo(String info) {
        infos.add(info);
    }

    public void setColumns(String... columns) {
        table = new XlsxTable<>();
        table.addColumns(columns);
    }

    @Override
    public void write(VarCollection vcoll) throws IOException {
        if(entry2attribute == null) {
            throw new NullPointerException("missing mapping function");
        }

        for(Object[] entry: vcoll.iterable()) {
            Attribute[] attributes = entry2attribute.apply(entry);
            table.addRow(attributes);
        }

        write();
    }

    public void write() throws IOException {
        if(table == null) {
            throw new IllegalStateException("no table created");
        }

        XlsxSheetWriter<Attribute> writer = new XlsxSheetWriter<>();
        writer.setTextConverter(StandardCellValueConverter.ATTR2STR);
        writer.setNumericConverter(StandardCellValueConverter.ATTR2NUM);
        writer.write(
                target,
                sheetName,
                infos,
                table.getHeaderInstance(),
                table.listTable()
        );
    }
}
