package com.example.sensorapp;

public class Timer {

    /* App specific runtime functions and variables (Not currently in use) */
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void startTime() {
        this.startTime = System.nanoTime();
        this.running = true;
    }


    public void stopTime() {
        this.stopTime = System.nanoTime();
        this.running = false;
    }


    /* In milliseconds */
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.nanoTime() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    /* In seconds */
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
