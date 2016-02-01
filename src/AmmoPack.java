package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class AmmoPack extends Entity{

    private boolean collected;

    public AmmoPack(float x, float y, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, "ammo", map, world);
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
