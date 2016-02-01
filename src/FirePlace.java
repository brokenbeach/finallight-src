package com.tedigc.ggj;

import com.badlogic.gdx.graphics.Color;

public class FirePlace extends Entity {


    private Light light;
    private float fuel = 100;
    private Timer particleTimer;


    public FirePlace(float x, float y, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, "firewood", map, world);
        // HARDCODED
//        light = new PulsingLight(x + sprite.getWidth() / 2, (map.getHeight() * Tile.SIZE/ 2) - y - 460, 80, true);
        light = new PulsingLight(x + sprite.getWidth() / 2, -570, 80, true);

        world.addLight(light);
        particleTimer = new Timer(50, true);
    }


    public boolean tick(float dt) {

        this.fuel -= dt * 0.6;
        this.light.setRadius(fuel + 20);
        if(fuel <= 0) {
            world.lose(1);
            return true;
        } else {
            if(particleTimer.tick(dt)) {
                float x = pos.x + (float) (Math.random() * width);
                float y = pos.y + (float) (Math.random() * 5) + 3;
                float dx = (float) ((Math.random() - 0.5) * 2f);
                float dy = (float) (Math.random()) * 20f;
                float size = (float) (Math.random() * 1) + 2;
                Color color = (Math.random() < 0.5) ? Color.ORANGE : Color.GOLD;
                world.addParticle(new Particle(x, y, dx, dy, 0, 0, 20, 20, size, color));
            }
        }
        return false;
    }

    public Light getLight() {

        return this.light;
    }


    public void addFuel(float fuel) {

        this.fuel += fuel;
    }


}
