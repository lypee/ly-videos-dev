package com.ly.common.org.n3r.idworker;

import com.ly.common.org.n3r.idworker.strategy.DefaultWorkerIdStrategy;

public class Id {
    private static com.ly.common.org.n3r.idworker.WorkerIdStrategy workerIdStrategy;
    private static com.ly.common.org.n3r.idworker.IdWorker idWorker;

    static {
        configure(DefaultWorkerIdStrategy.instance);
    }

    public static synchronized void configure(com.ly.common.org.n3r.idworker.WorkerIdStrategy custom) {
        if (workerIdStrategy == custom) return;

        if (workerIdStrategy != null) workerIdStrategy.release();
        workerIdStrategy = custom;
        workerIdStrategy.initialize();
        idWorker = new com.ly.common.org.n3r.idworker.IdWorker(workerIdStrategy.availableWorkerId());
    }

    public static long next() {
        return idWorker.nextId();
    }

    public static long getWorkerId() {
        return idWorker.getWorkerId();
    }
}
