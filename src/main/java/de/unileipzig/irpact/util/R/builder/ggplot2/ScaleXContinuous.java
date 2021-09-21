package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.ArrayElement;
import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class ScaleXContinuous extends Component {

    public ScaleXContinuous() {
    }

    @Override
    protected void init() {
        init(VALUES);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("scale_x_continuous", settings, target);
    }

    public void setBreaks(String... elements) {
        if(elements != null && elements.length > 0) {
            set(BREAKS, new ArrayElement(elements, true, false));
        }
    }

    public ArrayElement getBreaks() {
        return (ArrayElement) get(BREAKS);
    }
}
