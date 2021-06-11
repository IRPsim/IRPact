package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.ArrayElement;
import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class ScaleLinetypeManual extends Component {

    public ScaleLinetypeManual() {
    }

    @Override
    protected void init() {
        init(VALUES);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("scale_linetype_manual", settings, target);
    }

    public void setValues(String... elements) {
        set(VALUES, new ArrayElement(elements, false, true));
    }

    public ArrayElement getValues() {
        return (ArrayElement) get(VALUES);
    }
}
