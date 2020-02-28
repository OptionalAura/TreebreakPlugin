/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Action;

/**
 *
 * @author Daniel Allen
 */
public abstract class Scheduler implements Cloneable {

    private static boolean timerRunning = false;
    private static final List<Scheduler> MARKERS = new ArrayList<>();
    private static Timer timer;

    private Action finishAction,
            tickAction;

    /**
     * Increments all markers in queue
     */
    private static void increment() {
        for (int i = MARKERS.size() - 1; i >= 0; i--) {
            Scheduler m = MARKERS.get(i);
            if (!m.isPaused()) {
                m.tick();
                m.timeRemaining -= m.timeSinceLast();
                if (m.timeRemaining <= 0) {
                    m.done = true;
                    m.endMillis = System.currentTimeMillis();
                    m.finish();
                    m.destroy();
                }
            }
            m.lastMillis = System.currentTimeMillis();
        }
    }

    /**
     * Starts the timer, if it isn't already running
     */
    public static void startTimer() {
        if (timerRunning == false) {
            timer = new Timer();
            timerRunning = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    increment();
                }
            }, timerTickSpeed, timerTickSpeed);
        }
    }

    /**
     * Utility method to convert milliseconds to seconds
     *
     * @param ms
     * @return <code>ms/1000</code>
     */
    public static double convertToSeconds(long ms) {
        return (double) ms / 1000d;
    }

    /**
     * Returns true if the scheduler has any items that are not paused. In
     * effect, this will return <code>false</code> only if
     *
     * @return
     */
    public static boolean hasUnpausedItemsInTimer() {
        if (MARKERS.isEmpty()) {
            return false;
        }
        for (int i = 0; i < MARKERS.size(); i++) {
            if (!MARKERS.get(i).isPaused()) {
                return true;
            }
        }
        return false;
    }
    private boolean paused = true;

    private long timeRemaining;
    private long initialTimeRemaining;
    private long startMillis;
    private long endMillis;
    private long lastMillis;
    private boolean done = false;
    private boolean destroyed = false;

    public Scheduler(long duration) {
        this.timeRemaining = initialTimeRemaining = duration;
        this.lastMillis = System.currentTimeMillis();
        startMillis = System.currentTimeMillis();
    }

    private static long timerTickSpeed = 10;

    /**
     * Sets the update interval of the timer. The duration provided in
     * {@link getElapsedTime()} will always return the same value regardless of
     * this.
     *
     * @param ms
     */
    public static void setTimerTickSpeed(long ms) {
        stopTimer();
        timerTickSpeed = ms;
        startTimer();
    }

    /**
     * Starts this scheduler if not running, and resumes this instance.
     */
    public void start() {
        this.paused = false;
        if (!registered) {
            register();
        }
        if (timerRunning == false) {
            timer = new Timer();
            timerRunning = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    increment();
                }
            }, timerTickSpeed, timerTickSpeed);
        }
    }

    /**
     * Unpauses all items in the scheduler and starts running
     */
    public static void startAll() {
        for (Scheduler t : MARKERS) {
            t.paused = false;
        }
        if (timerRunning == false) {
            timer = new Timer();
            timerRunning = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    increment();
                }
            }, timerTickSpeed, timerTickSpeed);
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public abstract void finish();

    private boolean registered = false;

    public void register() {
        if (!registered && MARKERS.add(this)) {
            registered = true;
        }
    }

    public void unregister() {
        if (registered && MARKERS.remove(this)) {
            registered = false;
        }
    }

    public long getLength() {
        return this.initialTimeRemaining;
    }

    public long getElapsedTime() {
        if (done) {
            return endMillis - startMillis;
        }
        return System.currentTimeMillis() - startMillis;
    }

    public long getTimeRemaining() {
        return this.timeRemaining;
    }

    public boolean isDone() {
        return this.done;
    }

    public double getAmountDone() {
        return this.timeRemaining / this.initialTimeRemaining;
    }

    public abstract void tick();

    public void reset() {
        this.timeRemaining = initialTimeRemaining;
        done = false;
        destroyed = false;
        startTimer();
    }

    public void destroy() {
        MARKERS.remove(this);
        destroyed = true;
        if (!hasUnpausedItemsInTimer()) {
            stopTimer();
        }
    }

    public long timeSinceLast() {
        long time = System.currentTimeMillis() - lastMillis;
        return time;
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timerRunning = false;
    }

    @Override
    public Scheduler clone() throws CloneNotSupportedException {
        return (Scheduler) super.clone();
    }
}
