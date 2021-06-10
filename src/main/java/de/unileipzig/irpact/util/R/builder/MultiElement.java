package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class MultiElement implements Element {

    protected List<Element> elements = new ArrayList<>();

    public MultiElement(Element... elements) {
        Collections.addAll(this.elements, elements);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void add(Element element) {
        elements.add(element);
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        if(elements.isEmpty()) {
            return false;
        }

        boolean printed = false;
        for(Element element: elements) {
            if(printed) {
                target.append(settings.getElementLinker());
            }

            if(element.print(settings, target)) {
                printed = true;
            }
        }
        return printed;
    }
}
