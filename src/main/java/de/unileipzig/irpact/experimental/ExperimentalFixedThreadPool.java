package de.unileipzig.irpact.experimental;

import jadex.commons.ChangeEvent;
import jadex.commons.IChangeListener;
import jadex.commons.concurrent.IThreadPool;
import jadex.commons.future.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Daniel Abitz
 */
//Grundidee aus jadex.commons.concurrent.JavaThreadPool
public class ExperimentalFixedThreadPool implements IThreadPool {

    protected ExecutorService executor;
    protected List<IChangeListener<Void>> listeners;
    protected Future<Void> shutdown;

    public ExperimentalFixedThreadPool(
            boolean daemon,
            int cores) {
        ThreadFactory factory = runnable -> {
            Thread ret = new Thread(runnable) {
                @Override
                public String toString() {
                    return "XXX " + super.toString()+":"+hashCode()+", task="+runnable;
                }
            };
            System.out.println("TEST");
            ret.setDaemon(true);
            return ret;
        };

        executor = Executors.newFixedThreadPool(cores, factory);

        shutdown = new Future<>();

        Thread shutdownthread = new Thread(() -> {
            shutdown.get();
            executor.shutdown();
            try {
                executor.awaitTermination(10000, TimeUnit.MILLISECONDS);	// Hack???
            } catch(Exception e) {
                //ignore
            }
            notifyFinishListeners();
        });
        shutdownthread.setDaemon(daemon);
        shutdownthread.start();
    }

    protected void notifyFinishListeners() {
        List<IChangeListener<Void>> lisar;
        synchronized(this) {
            lisar = listeners == null
                    ? null
                    : new ArrayList<>(listeners);
        }
        if(lisar != null) {
            ChangeEvent<Void> ce = new ChangeEvent<>(null);
            for(IChangeListener<Void> lis: lisar) {
                lis.changeOccurred(ce);
            }
        }
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public void executeForever(Runnable task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void dispose() {
        shutdown.setResultIfUndone(null);
    }

    @Override
    public boolean isRunning() {
        return !executor.isShutdown();
    }

    @Override
    public void addFinishListener(IChangeListener<Void> listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }
}