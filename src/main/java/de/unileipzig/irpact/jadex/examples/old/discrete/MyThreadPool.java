package de.unileipzig.irpact.jadex.examples.old.discrete;

import de.unileipzig.irpact.commons.annotation.ToDo;
import jadex.commons.ChangeEvent;
import jadex.commons.IChangeListener;
import jadex.commons.concurrent.IThreadPool;
import jadex.commons.future.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//JavaThreadPool
@ToDo("naeher angucken")
public class MyThreadPool implements IThreadPool {

    //more hack

    public static ExecutorService asd(int size, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, size,
                60L,TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
    }

    //-------- attributes --------

    /** The executor service. */
    protected ExecutorService executor;

    /** The finished listeners. */
    protected List<IChangeListener<Void>> listeners;

    /** Future used for performing shutdown. */
    protected Future<Void> shutdown;

    //-------- constructors --------

    /**
     *  Create a new ThreadPool5.
     */
    public MyThreadPool(boolean daemon) {
        //executor = Executors.newCachedThreadPool(new ThreadFactory() {
        executor = asd(10, new ThreadFactory() {
            public Thread newThread(final Runnable r) {
                Thread ret = new Thread(r) {
                    /**
                     *  Get the string representation.
                     */
                    public String toString()
                    {
                        return super.toString()+":"+hashCode()+", task="+r;
                    }
                };
                ret.setDaemon(true);
                return ret;
            }
        });

        shutdown = new Future<>();

        Thread shutdownthread = new Thread(() -> {
            shutdown.get();

            executor.shutdown();
            //noinspection CatchMayIgnoreException
            try {
                executor.awaitTermination(10000, TimeUnit.MILLISECONDS);	// Hack???
            } catch(Exception e) {
            }
            notifyFinishListeners();
        });
        shutdownthread.setDaemon(daemon);
        shutdownthread.start();
    }

    //-------- IThreadPool interface --------

    /**
     *  Execute a task in its own thread.
     *  @param task The task to execute.
     */
    public void execute(Runnable task)
    {
        executor.execute(task);
    }

    /**
     *  Execute a task in its own thread.
     *  The pool expects the thread executing the task to never return.
     *  Preferably use this method if you want to permanently retrieve
     *  a thread e.g. for repeated blocking operations.
     *
     *  @param task The task to execute.
     */
    public void executeForever(Runnable task)
    {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    /**
     *  Shutdown the thread pool.
     */
    public void dispose()
    {
        shutdown.setResultIfUndone(null);
    }


    /**
     *  Test if the thread pool is running.
     */
    public boolean	isRunning()
    {
        return !executor.isShutdown();
    }

    /**
     *  Add a finish listener;
     */
    public synchronized void addFinishListener(IChangeListener<Void> listener)
    {
        if(listeners==null)
            listeners = new ArrayList<>();
        listeners.add(listener);
    }

    /**
     *  Notify the finish listeners.
     */
    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument", "unchecked"})
    protected void notifyFinishListeners()
    {
        IChangeListener<Void>[] lisar;
        synchronized(this)
        {
            lisar = listeners==null? null: listeners.toArray(new IChangeListener[listeners.size()]);
        }

        // Do not notify listeners in synchronized block
        if(lisar!=null)
        {
            ChangeEvent<Void> ce = new ChangeEvent<>(null);
            for(IChangeListener<Void> lis: lisar)
            {
                lis.changeOccurred(ce);
            }
        }
    }
}
