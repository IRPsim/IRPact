package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class ReadDotCsv extends Component {

    protected String returnName;

    public ReadDotCsv(String returnName) {
        this.returnName = returnName;
    }

    public String getReturnName() {
        return returnName;
    }

    @Override
    protected void init() {
        init(null);
        init(HEADER);
        init(SEP);
        init(COL_CLASSES);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        target.append(returnName);
        target.append(settings.getAssignmentSign());
        target.append("read.csv(");
        appendData(settings, target);
        target.append(")");
    }

    public void setFileName(String name) {
        set(null, name);
    }
    public String getFileName() {
        return getString(null);
    }

    public void setHeader(String header) {
        setIgnoreQuote(HEADER, header);
    }
    public void setHeader(boolean header) {
        setHeader(header ? "TRUE" : "FALSE");
    }
    public String getHeader() {
        return getString(HEADER);
    }

    public void setSep(String sep) {
        setForceQuote(SEP, sep);
    }
    public String getSep() {
        return getString(SEP);
    }

    public void setColClasses(String... classes) {
        if(classes != null && classes.length > 0) {
            set(COL_CLASSES, new ColClasses(classes));
        }
    }
    public Element getColClasses() {
        return get(COL_CLASSES);
    }
}
