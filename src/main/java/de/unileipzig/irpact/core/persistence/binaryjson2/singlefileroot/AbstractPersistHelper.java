package de.unileipzig.irpact.core.persistence.binaryjson2.singlefileroot;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irpact.commons.persistence.SimpleUIDManager;
import de.unileipzig.irpact.core.persistence.binaryjson2.*;
import de.unileipzig.irpact.core.persistence.binaryjson2.annotation.AnnotatedClass;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractPersistHelper implements PersistManager, PersistHelper {

    protected String name;
    protected SimpleUIDManager uidManager = new SimpleUIDManager();
    protected Map<Object, Long> uids = new HashMap<>();
    protected Map<Class<?>, BinaryPersister<?>> persisters = new HashMap<>();
    protected Set<Object> called = new HashSet<>();
    protected Set<Class<?>> missingPersisters = new HashSet<>();
    protected ClassManager classManager = new ClassManager();

    protected boolean autoCallPersist = true;

    public AbstractPersistHelper() {
        this(StandardSettings.DEFAULT_NAME);
    }

    public AbstractPersistHelper(String name) {
        this.name = name;
    }

    //=========================
    //util
    //=========================

    public void setName(String name) {
        this.name = name;
    }

    protected <R> BinaryPersister<R> findPersister(Object obj) {
        return getPersister(obj.getClass());
    }

    protected void validate(Object obj) {
        if(!persisters.containsKey(obj.getClass())) {
            throw new NoSuchElementException("missing persister for class: " + obj.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    protected <R> BinaryPersister<R> getPersister(Class<?> c) {
        BinaryPersister<R> persister = (BinaryPersister<R>) persisters.get(c);
        if(persister == null) {
            throw new NullPointerException("missing persister for: " + c);
        }
        return persister;
    }

    protected long getUid0(Object obj, boolean persist) throws Throwable {
        if(obj == null) {
            throw new NullPointerException("obj is null");
        }

        Long uid = uids.get(obj);
        if(uid == null) {
            validate(obj);
            uid = uidManager.nextUID();
            uids.put(obj, uid);
            if(persist) {
                persist0(obj);
            }
        }
        return uid;
    }

    @SuppressWarnings("unchecked")
    protected <I> I asType(Object obj) {
        return (I) obj;
    }

    protected <I> void persist0(Object obj) throws Throwable {
        if(called.add(obj)) {
            long uid = getUid0(obj, false);
            BinaryPersister<I> persister = findPersister(obj);
            I input = asType(obj);
            ArrayNode node = getNode(uid);
            persister.persist(input, this, node);
            handleNode(uid, node);
        }
    }

    protected <I> void peek0(Object obj) {
        try {
            if(called.add(obj)) {
                BinaryPersister<I> persister = findPersister(obj);
                I input = asType(obj);
                persister.peek(input, this);
            }
        } catch (NullPointerException t) {
            missingPersisters.add(obj.getClass());
        }
    }

    protected abstract ArrayNode getNode(long uid);

    protected abstract void handleNode(long uid, ArrayNode node);

    protected void resetForNewStart() {
        missingPersisters.clear();
        called.clear();
    }

    //=========================
    //access
    //=========================

    public void register(AnnotatedClass persister) {
        if(persister.isScanned()) {
            persisters.put(persister.getType(), persister);
        } else {
            throw new IllegalArgumentException("not scanned");
        }
    }

    public <T> void register(Class<T> type, BinaryPersister<T> persister) {
        persisters.put(type, persister);
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    //=========================
    //Helper
    //=========================

    @Override
    public long getClassId(Class<?> c) {
        return classManager.getId(c);
    }

    @Override
    public void peek(Object obj) {
        peek0(obj);
    }

    @Override
    public long getUid(Object obj) throws Throwable {
        return getUid0(obj, autoCallPersist);
    }

    //=========================
    //Manager
    //=========================

    @Override
    public void reset() {
        persisters.clear();
        uids.clear();
        classManager.reset();
        uidManager.setNextUID(0);
    }

    @Override
    public Set<Class<?>> findMissingPersisters(Object obj) {
        resetForNewStart();
        return new HashSet<>(missingPersisters);
    }

    @Override
    public void persist(Object obj) throws Throwable {
        resetForNewStart();
        getUid0(obj, true);
    }
}
