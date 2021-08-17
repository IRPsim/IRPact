package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.ArrayElement;
import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.Element;
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

    public void setTwoNamend(String label0, String type0, String label1, String type1) {
        //hmm... schoener machen
        set(VALUES, new Element() {
            @Override
            public boolean isPrintable() {
                return true;
            }

            @Override
            public boolean print(StringSettings settings, Appendable target) throws IOException {
                target.append("c(");
                target.append(settings.quote(label0));
                target.append(settings.getEqualSign());
                target.append(settings.quote(type0));
                target.append(settings.getElementDelimiter());
                target.append(settings.quote(label1));
                target.append(settings.getEqualSign());
                target.append(settings.quote(type1));
                target.append(")");
                return true;
            }
        });
    }

    public ArrayElement getValues() {
        return (ArrayElement) get(VALUES);
    }
}
