package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;
import java.util.Deque;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface MultiModule2<I, O> extends Module2<I, O> {

    int getSubmoduleCount();

    <A, B> Module2<A, B> getSubmodule(int index);

    static Module2<?, ?> containsLoop(
            Deque<Module2<?, ?>> currentPath,
            Set<Module2<?, ?>> allModules,
            Module2<?, ?> master,
            Module2<?, ?>... submodules) {
        allModules.add(master);
        if(currentPath.contains(master)) {
            return master;
        } else {
            currentPath.add(master);
            for(Module2<?, ?> submodule: submodules) {
                Module2<?, ?> duplicate = submodule.containsLoop(currentPath, allModules);
                if(duplicate != null) {
                    return duplicate;
                }
            }
            Module2<?, ?> last = currentPath.removeLast();
            if(last != master) {
                throw new IllegalStateException("not master");
            }
            return null;
        }
    }

    static void validateAll(Module2<?, ?>... modules) throws Throwable {
        for(Module2<?, ?> module: modules) {
            module.validate();
        }
    }

    static void initalizeAll(SimulationEnvironment environment, Module2<?, ?>... modules) throws Throwable {
        for(Module2<?, ?> module: modules) {
            module.initialize(environment);
        }
    }

    static void setSharedDataAll(SharedModuleData sharedData, Module2<?, ?>... modules) {
        for(Module2<?, ?> module: modules) {
            module.setSharedData(sharedData);
        }
    }

    @Override
    default Module2<?, ?> containsLoop(Deque<Module2<?, ?>> currentPath, Set<Module2<?, ?>> allModules) {
        allModules.add(this);
        if(currentPath.contains(this)) {
            return this;
        } else {
            currentPath.add(this);
            for(int i = 0; i < getSubmoduleCount(); i++) {
                Module2<?, ?> submodule = getSubmodule(i);
                Module2<?, ?> duplicate = submodule.containsLoop(currentPath, allModules);
                if(duplicate != null) {
                    return duplicate;
                }
            }
            Module2<?, ?> last = currentPath.removeLast();
            if(last != this) {
                throw new IllegalStateException("not master");
            }
            return null;
        }
    }
}
