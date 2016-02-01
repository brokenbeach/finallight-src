package com.tedigc.ggj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Wood extends Entity {

    private boolean collected;

    public Wood(float x, float y, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, "firewood", map, world);
        this.collected = false;
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        if(!collected)
            sprite.draw(batch);
    }

    public void pickUp(){

        collected = true;
        world.main.collect.play();
        world.fireplace.addFuel(10f);
        world.main.collect.play();
    }

    public boolean isCollected(){

        return collected;
    }

}
