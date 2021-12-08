package de.unileipzig.irpact.util.scenarios.util;

import de.unileipzig.irpact.io.param.input.process2.modular.InModule2;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class ModuleManager {

    protected final Map<String, InModule2> modules = new HashMap<>();

    public ModuleManager() {
    }

    public boolean has(String name) {
        return modules.containsKey(name);
    }

    public InModule2 findModule(String name) {
        InModule2 module = modules.get(name);
        if(module == null) {
            throw new NoSuchElementException("missing: " + name);
        }
        return module;
    }

    public void register(InModule2 module) {
        if(modules.containsKey(module.getName())) {
            throw new IllegalArgumentException("already exists: " + module.getName());
        }
        modules.put(module.getName(), module);
    }
    
    public <R extends InModule2> R create(String name, Function<? super String, ? extends R> func) {
        R module = func.apply(name);
        register(module);
        return module;
    }

    public <R extends InModule2> R findModule(String name, Class<R> type) {
        return type.cast(findModule(name));
    }

    @SuppressWarnings("unchecked")
    public <R extends InModule2> R findModuleAuto(String name) {
        return (R) findModule(name);
    }

    @SuppressWarnings("unchecked")
    public <R extends InModule2> R registerIfNotExists(String name, Function<? super String, ? extends R> func) {
        return (R) modules.computeIfAbsent(name, func);
    }
}
