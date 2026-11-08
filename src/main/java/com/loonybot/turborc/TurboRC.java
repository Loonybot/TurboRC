package com.loonybot.turborc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Encourages the CPU scheduler to raise the clock by running background workers during an opMode.
 */
@SuppressWarnings("unused")
public final class TurboRC {
    private static final int DEFAULT_THREAD_COUNT = 1;
    private static volatile long sink;

    private final LinearOpMode opMode;
    private final Thread[] workerThreads;
    private volatile boolean stopped;

    /**
     * Creates a TurboRC instance with one worker. Pass {@code this} from a LinearOpMode.
     */
    public TurboRC(@NonNull LinearOpMode opMode) {
        this(opMode, DEFAULT_THREAD_COUNT);
    }

    /**
     * Creates a TurboRC instance with between one and three workers.
     *
     * @param opMode the active LinearOpMode
     * @param workerCount the number of background workers
     */
    public TurboRC(@NonNull LinearOpMode opMode, int workerCount) {
        if (workerCount < 1 || workerCount > 3) {
            throw new IllegalArgumentException("workerCount must be between 1 and 3");
        }

        this.opMode = opMode;
        workerThreads = new Thread[workerCount];
        for (int i = 0; i < workerCount; ++i) {
            workerThreads[i] = new Thread(this::burnerThread, "TurboRC-" + i);
            workerThreads[i].setPriority(Thread.NORM_PRIORITY - 1);
            workerThreads[i].start();
        }
    }

    /**
     * Stops the workers early, including before Start. The return value can be ignored.
     */
    public long stop() {
        stopped = true;
        for (Thread thread : workerThreads) {
            thread.interrupt();
        }
        for (Thread thread : workerThreads) {
            try {
                thread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return sink;
    }

    private void burnerThread() {
        while (!stopped && !opMode.isStarted() && !opMode.isStopRequested()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        if (stopped || opMode.isStopRequested()) {
            return;
        }
        Log.i("TurboRC", "Burner thread started!");

        long x = 0x12345678;
        long startTime = System.nanoTime();
        while (System.nanoTime() - startTime < 120_000_000_000L
                && !stopped && !opMode.isStopRequested()) {
            for (int i = 0; i < 1000; ++i) {
                x = x * 1664525L + 1013904223L;
                x ^= x >>> 13;
            }
        }
        sink = x;
        Log.i("TurboRC", "Burner thread done!");
    }
}
