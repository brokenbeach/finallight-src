package com.tedigc.ggj;

public class TimedLight extends Light {


    private Timer timer;


    public TimedLight(float x, float y, float radius, float duration) {

        super(x, y, radius);
        this.timer = new Timer(duration, true);
    }

    public TimedLight(float x, float y, float radius, boolean bright, float duration) {

        super(x, y, radius, bright);
        this.timer = new Timer(duration, true);
    }


    public boolean tick(float dt) {

        if(timer.tick(dt)) {
            return true;
        }
        return false;
    }


}
