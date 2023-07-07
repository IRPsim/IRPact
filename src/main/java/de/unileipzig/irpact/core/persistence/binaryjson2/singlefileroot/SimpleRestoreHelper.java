package de.unileipzig.irpact.core.persistence.binaryjson2.singlefileroot;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irpact.core.persistence.binaryjson2.*;
import de.unileipzig.irpact.core.persistence.binaryjson2.annotation.AnnotatedClass;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class SimpleRestoreHelper implements RestoreManager, RestoreHelper {

    protected String name;
    protected ClassManager classManager = new ClassManager();
    protected Map<Long, ArrayNode> nodes = new HashMap<>();
    protected Map<Long, Object> restored = new HashMap<>();
    protected Map<Class<?>, BinaryRestorer<?>> restorers = new HashMap<>();
    protected Set<Long> finished = new HashSet<>();

    public SimpleRestoreHelper() {
        this(StandardSettings.DEFAULT_NAME);
    }

    public SimpleRestoreHelper(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setClassManager(ClassManager classManager) {
        this.classManager = classManager;
    }

    public void setRoot(ArrayNode root, int startIndex) {
        int i = 0;
        while(i + startIndex < root.size()) {
            ArrayNode child = (ArrayNode) root.get(i + startIndex);
            nodes.put((long) i, child);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> BinaryRestorer<T> getRestorer(long uid) {
        Class<?> c = getClass(uid);
        BinaryRestorer<T> restorer = (BinaryRestorer<T>) restorers.get(c);
        if(restorer == null) {
            throw new IllegalArgumentException("no restorer for: " + uid);
        }
        return restorer;
    }

    protected Class<?> getClass(long uid) {
        ArrayNode arr = nodes.get(uid);
        if (arr == null) {
            throw new IllegalArgumentException("unknown uid: " + uid);
        }
        long classId = IterableArrayNode.getClassId(arr);
        Class<?> c = classManager.getClass(classId);
        if(c == null) {
            throw new IllegalArgumentException("unknown uid: " + uid);
        }
        return c;
    }

    protected Object newInstance(long uid) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return getClass(uid).getDeclaredConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    protected <I> I asType(Object obj) {
        return (I) obj;
    }

    public void register(AnnotatedClass persister) {
        if(persister.isScanned()) {
            restorers.put(persister.getType(), persister);
        } else {
            throw new IllegalArgumentException("not scanned");
        }
    }

    //=========================
    // helper
    //=========================

    @Override
    public Object restore(long uid) throws Throwable {
        Object value = restored.get(uid);
        if(value == null) {
            value = newInstance(uid);
            restored.put(uid, value);
        }
        return value;
    }

    //=========================
    // manager
    //=========================

    @Override
    public Object restoreInstance() throws Throwable {
        return restoreInstance(0);
    }

    @Override
    public Object restoreInstance(long uid) throws Throwable {
        Object value = restored.get(uid);
        if(value == null) {
            run();
            value = restored.get(uid);
            if(value == null) {
                throw new IllegalArgumentException("unused uid: " + uid);
            }
        }
        return value;
    }

    private void run() throws Throwable {
        runInitalize();
        runRestore();
    }

    protected void runInitalize() throws Throwable {
        for(Long uid: nodes.keySet()) {
            restore(uid);
        }
    }

    protected void runRestore() throws Throwable {
        for(Long uid: nodes.keySet()) {
            runRestore(uid);
        }
    }

    protected <T> void runRestore(long uid) throws Throwable {
        if(finished.add(uid)) {
            BinaryRestorer<T> restorer = getRestorer(uid);
            ArrayNode node = nodes.get(uid);
            T value = asType(restored.get(uid));
            restorer.restore(value, this, node);
        }
    }
}
