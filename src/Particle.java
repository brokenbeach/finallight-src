package com.tedigc.ggj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Particle {


    protected Vector2 pos;
    protected Vector2 vel;
    protected Vector2 acc;
    protected float age;
    protected float lifespan;
    protected float size;
    protected Color color;


    public Particle(float x, float y, float dx, float dy, float ddx, float ddy, float age, float lifespan, float size, Color color) {

        this.pos = new Vector2(x, y);
        this.vel = new Vector2(dx, dy);
        this.acc = new Vector2(ddx, ddy);
        this.age = age;
        this.lifespan = lifespan;
        this.size = size;
        this.color = color;
    }


    public boolean tick(float dt) {

        age += dt;
        size -= dt;
        if(size < 1f) {
            return true;
        }

        vel.x += (0 - vel.x) * 0.1f * dt;
        vel.y += (0 - vel.y) * 0.1f * dt;
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;
        return false;
    }


    public void draw(ShapeRenderer renderer) {

        renderer.setColor(color);
        renderer.circle(pos.x, pos.y, size);
    }



}
