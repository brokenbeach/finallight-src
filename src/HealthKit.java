package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by marcosss3 on 31/1/16.
 */
public class HealthKit extends Entity{

    private boolean collected;

    public HealthKit(float x, float y, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, "healthkit", map, world);
        this.collected = false;
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        if(!collected)
            sprite.draw(batch);
    }

    public void pickUp(){

        collected = true;
        world.main.collect.play();
    }

    public boolean isCollected(){

        return collected;
    }

}
