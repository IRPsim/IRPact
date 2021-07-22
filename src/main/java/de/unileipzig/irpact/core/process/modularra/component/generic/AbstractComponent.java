package de.unileipzig.irpact.core.process.modularra.component.generic;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractComponent extends NameableBase implements Component {

    protected ComponentType type;

    protected AbstractComponent(ComponentType type) {
        this.type = type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    @Override
    public ComponentType getType() {
        return type;
    }

    @Override
    public Collection<Component> getAllComponents() {
        Set<Component> components = new HashSet<>();
        for(Component component: iterateComponents()) {
            components.addAll(component.getAllComponents());
        }
        return components;
    }

    @Override
    public boolean isCyclic() {
        Collection<Component> components = getAllComponents();
        if(components.isEmpty()) {
            return false;
        }

        Map<Component, Boolean> visited = new HashMap<>();
        Map<Component, Boolean> recStack = new HashMap<>();

        for(Component component: components) {
            visited.put(component, false);
            recStack.put(component, false);
        }

        for(Component component: components) {
            if(isCyclic(component, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isCyclic(
            Component current,
            Map<Component, Boolean> visited,
            Map<Component, Boolean> recStack) {
        if(recStack.get(current)) {
            return true;
        }

        if(visited.get(current)) {
            return false;
        }

        visited.put(current, true);

        recStack.put(current, true);
        for(Component child: current.iterateComponents()) {
            if(isCyclic(child, visited, recStack)) {
                return true;
            }
        }
        recStack.put(current, false);

        return false;
    }
}
