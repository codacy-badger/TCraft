package com.tincher.tcraftlib.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by dks on 2018/3/9.
 */

public final class ScheduledTimer {
    public static final long     DEFAULT_DELAY = 0;
    public final static TimeUnit DEFAULT_UNIT  = TimeUnit.SECONDS;
    private             long     mDelay        = DEFAULT_DELAY;
    private             Runnable mCommand      = null;
    private TimeUnit mUnit;

    private final ScheduledExecutorService scheduledTimer  =
            Executors.newSingleThreadScheduledExecutor(new ScheduledTimer.DaemonThreadFactory());
    private       ScheduledFuture<?>       scheduledFuture = null;

    public ScheduledTimer(Runnable command, long delay, TimeUnit unit) {
        this.mCommand = command;
        this.mDelay = delay;// null == delay ? DEFAULT_DELAY :
        this.mUnit = unit == null ? DEFAULT_UNIT : unit;
    }

    private void cancel() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    public void schedule() {
        cancel();
        scheduledFuture = scheduledTimer.schedule(mCommand, mDelay, mUnit);
    }

    public void shutdown() {
        cancel();
        scheduledTimer.shutdown();
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }
}
