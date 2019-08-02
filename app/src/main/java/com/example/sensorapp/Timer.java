package com.example.sensorapp;

/**
 * Helper class for application runtime if needed
 */
public class Timer {

    /* App specific runtime functions and variables (Not currently in use) */
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    /**
     * Starts timer
     */
    public void startTime() {
        this.startTime = System.nanoTime();
        this.running = true;
    }


    /**
     * Stops timer
     */
    public void stopTime() {
        this.stopTime = System.nanoTime();
        this.running = false;
    }


    /**
     * Gives elapsed time in milliseconds
     * @return elapsed time (ms)
     */
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.nanoTime() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    /**
     * Gives elapsed time in seconds
     * @return elapsed time (s)
     */
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000000000);
        } else {
            elapsed = ((stopTime - startTime) / 1000000000);
        }
        return elapsed;
    }

}
