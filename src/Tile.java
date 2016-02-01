package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Tile {


    public static final int SIZE = 16;
    private Sprite sprite;
    private int x;
    private int y;
    private int type;
    private boolean blocked;
    private boolean destructable;


    public Tile(int x, int y, int type) {

        this.x = x;
        this.y = y;
        this.type = type;
        switch(type) {
            case 0:
                blocked = false;
//                sprite = new Sprite(TextureStore.get().getTexture("tile_snow0"));
                break;
            case 1:
                blocked = true;
//                sprite = new Sprite(TextureStore.get().getTexture("tile_snow0"));
                break;
        }
//        sprite.setPosition(x * SIZE, y * SIZE);
    }


    public boolean tick(float dt) {

        // Tick here
        return false;
    }


    public void draw(SpriteBatch batch) {

//        sprite.draw(batch);
    }


    public boolean isBlocked() {

        return this.blocked;
    }


    public boolean isDestructable() {

        return this.destructable;
    }


}