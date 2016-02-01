package com.tedigc.ggj;

import com.badlogic.gdx.math.Vector2;

public class Light {


    protected Vector2 pos;
    protected float radius;
    protected boolean bright = true;


    public Light(float x, float y, float radius) {

        this.pos = new Vector2(x, y);
        this.radius = radius;
    }


    public Light(float x, float y, float radius, boolean bright) {

        this.pos = new Vector2(x, y);
        this.radius = radius;
        this.bright = bright;
    }


    public boolean tick(float dt) {

        return false;
    }


    public void setPosition(float x, float y) {

        this.pos.x = x;
        this.pos.y = y;
    }


    public float getX() {

        return this.pos.x;
    }


    public float getY() {

        return this.pos.y;
    }


    public float getRadius() {

        return this.radius;
    }


    public boolean isBright() {

        return this.bright;
    }


    public void setRadius(float radius) {

        this.radius = radius;
    }


}