package de.unileipzig.irpact.commons.util.xlsx2;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicDoubleAttribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.attribute.DataType;

/**
 * @author Daniel Abitz
 */
public class StandardCellValueConverter2 {

    public static CellValueConverter2<String, Attribute> STR2ATTR =
            (columnIndex, value) -> new BasicStringAttribute(value);

    public static CellValueConverter2<Number, Attribute> NUM2ATTR =
            (columnIndex, value) -> new BasicDoubleAttribute(value.doubleValue());

    public static final CellValueConverter2<Void, Attribute> BLANK2ATTR =
            (columnIndex, value) -> null;

    public static CellValueConverter2<Attribute, String> ATTR2STR = new CellValueConverter2<Attribute, String>() {
        @Override
        public boolean isSupported(int columnIndex, Attribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.STRING);
        }

        @Override
        public String convert(int columnIndex, Attribute value) {
            return value.asValueAttribute().getStringValue();
        }
    };
    public static final CellValueConverter2<Attribute, Number> ATTR2NUM = new CellValueConverter2<Attribute, Number>() {
        @Override
        public boolean isSupported(int columnIndex, Attribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.DOUBLE);
        }

        @Override
        public Number convert(int columnIndex, Attribute value) {
            return value.asValueAttribute().getDoubleValue();
        }
    };

    private StandardCellValueConverter2() {
    }

    public static XlsxSheetParser2<Attribute> newParser() {
        XlsxSheetParser2<Attribute> parser = new XlsxSheetParser2<>();
        parser.setNumericConverter(NUM2ATTR);
        parser.setTextConverter(STR2ATTR);
        parser.setBlankConverter(BLANK2ATTR);
        return parser;
    }
}
