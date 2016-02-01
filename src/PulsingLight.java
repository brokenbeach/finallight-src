package com.tedigc.ggj;

public class PulsingLight extends Light {


    private float currentRadius;
    private float rad = 0;

    public PulsingLight(float x, float y, float radius) {

        super(x, y, radius);
        this.currentRadius = radius;
    }

    public PulsingLight(float x, float y, float radius, boolean bright) {

        super(x, y, radius, bright);
        this.currentRadius = radius;
    }


    public boolean tick(float dt) {

        rad += dt;
        if(rad > Math.PI) rad = 0;
        currentRadius = radius + (float) Math.sin(rad) * 20f;
        return false;
    }


    public float getRadius() {

        return this.currentRadius;
    }


}
