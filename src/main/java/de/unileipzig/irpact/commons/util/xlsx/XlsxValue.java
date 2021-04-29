package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public interface XlsxValue {

    static XlsxSheetParser<XlsxValue> newParser() {
        XlsxSheetParser<XlsxValue> parser = new XlsxSheetParser<>();
        parser.setNumbericConverter(ForDouble.CONVERTER);
        parser.setTextConverter(ForString.CONVERTER);
        return parser;
    }

    DataType getDataType();

    double doubleValue();

    String stringValue();

    String printValue();

    /**
     * @author Daniel Abitz
     */
    class ForDouble implements XlsxValue {

        public static final CellValueConverter<Number, XlsxValue> CONVERTER =
                (columnIndex, header, value) -> new ForDouble(value.doubleValue());

        protected double value;

        public ForDouble() {
        }

        public ForDouble(double value) {
            this.value = value;
        }

        @Override
        public DataType getDataType() {
            return DataType.DOUBLE;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public String stringValue() {
            throw new UnsupportedOperationException("double");
        }

        @Override
        public String printValue() {
            return Double.toString(value);
        }

        @Override
        public String toString() {
            return printValue();
        }
    }

    /**
     * @author Daniel Abitz
     */
    class ForString implements XlsxValue {

        public static final CellValueConverter<String, XlsxValue> CONVERTER =
                (columnIndex, header, value) -> new ForString(value);

        protected String value;

        public ForString() {
        }

        public ForString(String value) {
            this.value = value;
        }

        @Override
        public DataType getDataType() {
            return DataType.STRING;
        }

        @Override
        public double doubleValue() {
            throw new UnsupportedOperationException("string");
        }

        @Override
        public String stringValue() {
            return value;
        }

        @Override
        public String printValue() {
            return value;
        }

        @Override
        public String toString() {
            return printValue();
        }
    }
}
