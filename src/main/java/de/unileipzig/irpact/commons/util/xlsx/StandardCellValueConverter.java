package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicDoubleAttribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.util.table.Header;

/**
 * @author Daniel Abitz
 */
public class StandardCellValueConverter {

    public static CellValueConverter<String, Attribute> STR2ATTR =
            (header, columnIndex, value) -> new BasicStringAttribute(header.getLabel(columnIndex), value);

    public static CellValueConverter<Number, Attribute> NUM2ATTR =
            (header, columnIndex, value) -> new BasicDoubleAttribute(header.getLabel(columnIndex), value.doubleValue());

    public static final CellValueConverter<Void, Attribute> EMPTY2ATTR =
            (header, columnIndex, value) -> null;

    public static CellValueConverter<Attribute, String> ATTR2STR = new CellValueConverter<Attribute, String>() {
        @Override
        public boolean isSupported(Header header, int columnIndex, Attribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.STRING);
        }

        @Override
        public String convert(Header header, int columnIndex, Attribute value) {
            return value.asValueAttribute().getStringValue();
        }
    };
    public static final CellValueConverter<Attribute, Number> ATTR2NUM = new CellValueConverter<Attribute, Number>() {

        @Override
        public boolean isSupported(Header header, int columnIndex, Attribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.DOUBLE);
        }

        @Override
        public Number convert(Header header, int columnIndex, Attribute value) {
            return value.asValueAttribute().getDoubleValue();
        }
    };

    private StandardCellValueConverter() {
    }
}
