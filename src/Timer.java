package com.tedigc.ggj;

public class Timer {


    private float delay   = 0;
    private float elapsed = 0;
    private boolean started = false;


    public Timer() {

        this(0);
    }


    public Timer(float delay) {

        this(delay, false);
    }


    public Timer(float delay, boolean started) {

        this.delay = delay;
        this.started = started;
    }


    public boolean tick(float dt) {

        if(started) {
            elapsed += dt * 1000;
            if(elapsed > delay) {
                elapsed = 0;
                return true;
            }
        }
        return false;
    }


    public void start() {

        this.started = true;
    }


    public void stop() {

        this.started = false;
    }


    public void resetTimer() {

        this.elapsed = 0;
    }


    public void setDelay(float delay) {

        this.delay = delay;
    }


    public float delay() {

        return this.delay;
    }


    public float elapsed() {

        return elapsed;
    }


}