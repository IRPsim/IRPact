package de.unileipzig.irpact.commons.util.fio2.xlsx2;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicDoubleAttribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.attribute.DataType;

/**
 * @author Daniel Abitz
 */
public class StandardCellValueConverter2 {

    public static CellValueConverter2<String, Attribute> STR2ATTR = new CellValueConverter2<String, Attribute>() {
        @Override
        public boolean isSupported(int columnIndex, String value) {
            return true;
        }

        @Override
        public Attribute convert(int columnIndex, String value) {
            return new BasicStringAttribute(value);
        }
    };

    public static CellValueConverter2<Number, Attribute> NUM2ATTR = new CellValueConverter2<Number, Attribute>() {
        @Override
        public boolean isSupported(int columnIndex, Number value) {
            return true;
        }

        @Override
        public Attribute convert(int columnIndex, Number value) {
            return new BasicDoubleAttribute(value.doubleValue());
        }
    };

    public static final CellValueConverter2<Void, Attribute> BLANK2ATTR = new CellValueConverter2<Void, Attribute>() {
        @Override
        public boolean isSupported(int columnIndex, Void value) {
            return true;
        }

        @Override
        public Attribute convert(int columnIndex, Void value) {
            return null;
        }
    };

    public static CellValueConverter2<Attribute, String> ATTR2STR = new CellValueConverter2<Attribute, String>() {
        @Override
        public boolean isSupported(int columnIndex, Attribute value) {
            return value.isValueAttributeWithDataType(DataType.STRING);
        }

        @Override
        public String convert(int columnIndex, Attribute value) {
            return value.asValueAttribute().getStringValue();
        }
    };

    public static final CellValueConverter2<Attribute, Number> ATTR2NUM = new CellValueConverter2<Attribute, Number>() {
        @Override
        public boolean isSupported(int columnIndex, Attribute value) {
            return value.isValueAttributeWithDataType(DataType.DOUBLE);
        }

        @Override
        public Number convert(int columnIndex, Attribute value) {
            return value.asValueAttribute().getDoubleValue();
        }
    };

    public static final CellValueConverter2<Attribute, Void> ATTR2BLANK = new CellValueConverter2<Attribute, Void>() {
        @Override
        public boolean isSupported(int columnIndex, Attribute value) {
            return value == null;
        }

        @Override
        public Void convert(int columnIndex, Attribute value) {
            return null;
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

    public static XlsxSheetWriter2<Attribute> newWriter() {
        XlsxSheetWriter2<Attribute> writer = new XlsxSheetWriter2<>();
        writer.setNumericConverter(ATTR2NUM);
        writer.setTextConverter(ATTR2STR);
        writer.setBlankConverter(ATTR2BLANK);
        return writer;
    }
}
