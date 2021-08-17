package de.unileipzig.irpact.start.irpact.executors;

import de.unileipzig.irpact.start.irpact.IRPactExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class IRPactExecutors {

    public static final String DEFAULT_MODE = RunFully.ID_STR;

    protected static final Map<Integer, IRPactExecutor> executors = new HashMap<>();
    public static boolean register(IRPactExecutor executor) {
        if(isRegistered(executor.id())) {
            return false;
        } else {
            executors.put(executor.id(), executor);
            return true;
        }
    }
    protected static void register0(IRPactExecutor executor) {
        if(!register(executor)) {
            throw new IllegalStateException("id " + executor.id() + " already exists (" + executor.getClass() + ")");
        }
    }
    public static boolean isRegistered(int id) {
        return executors.containsKey(id);
    }
    public static boolean isNotRegistered(int id) {
        return !isRegistered(id);
    }
    public static IRPactExecutor get(int id) {
        return executors.get(id);
    }
    public static IRPactExecutor find(int id) throws NoSuchElementException {
        IRPactExecutor exec = get(id);
        if(exec == null) {
            throw new NoSuchElementException("unsupported id: " + id);
        }
        return exec;
    }

    static {
        register0(RunFully.INSTANCE);
        register0(RunMinimalSimulation.INSTANCE);
        register0(TestMode.INSTANCE);
        register0(PrintInput.INSTANCE);
        register0(ThrowError.INSTANCE);
    }

    private IRPactExecutors() {
    }
}
