package com.tedigc.ggj;

public class Footprint extends Entity {


    private Timer timer;


    public Footprint(float x, float y, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, "footprint", map, world);
        this.timer = new Timer(30000, true);
    }


    public boolean tick(float dt) {

        return timer.tick(dt);
    }


}
