package de.unileipzig.irpact.core.process.mra.component;

import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ComponentGraphBuilder {

    protected static final String[] EMPTY = new String[0];

    protected MapSupplier supplier;
    protected Map<String, Component> components;
    protected Map<String, String[]> componentLinks;

    public ComponentGraphBuilder() {
        this(MapSupplier.HASH);
    }

    public ComponentGraphBuilder(MapSupplier supplier) {
        this.supplier = supplier;
        this.components = supplier.newMap();
        this.componentLinks = supplier.newMap();
    }

    public boolean register(Component component) {
        return register(component, EMPTY);
    }

    public boolean register(Component component, String[] links) {
        if(components.containsKey(component.getName())) {
            return false;
        } else {
            components.put(component.getName(), component);
            componentLinks.put(component.getName(), links);
            return true;
        }
    }

    public void clear() {
        components.clear();
        componentLinks.clear();
    }

    public List<Component> buildAll() {
        List<Component> list = new ArrayList<>();
        buildAll(list);
        return list;
    }

    public boolean buildAll(Collection<? super Component> out) {
        boolean changed = false;

        for(Component component: components.values()) {
            if(component.isOutput()) {
                changed = true;
                out.add(component);
            }

            String[] links = componentLinks.get(component.getName());
            for(String link: links) {
                Component linkComponent = components.get(link);
                if(!component.add(linkComponent)) {
                    throw new IRPactIllegalArgumentException("adding '{}' to '{}' failed", linkComponent.getName(), component.getName());
                }
            }
        }

        return changed;
    }
}
