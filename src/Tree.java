package com.tedigc.ggj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tree extends Entity {


    public Tree(float x, float y, String ref, TileMap map, MyScreen world) {

        super(x, y, 0, 0, 0, 0, ref, map, world);
    }


    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        // Remember old values
        float r = sprite.getColor().r;
        float g = sprite.getColor().g;
        float b = sprite.getColor().b;
        // If the tree is above the player, make it darker
        if(pos.y > world.player.pos.y) {
            float diff = pos.y - world.player.pos.y;
            float darkness = 0;
            if(diff > 100) {
                sprite.setColor(0f, 0f, 0f, 1f);
            } else {
                darkness = 1.2f - (diff / 100);
                if(darkness > 1f) darkness = 1;
                sprite.setColor(r * darkness, g * darkness, b * darkness, 1);
            }
        } else {
            // If it is below the player, change its alpha
            float diff = world.player.pos.y - pos.y;
            float alpha = 0;
            if(diff > 100) {
                sprite.setColor(0f, 0f, 0f, 0f);
            } else {
                alpha = 1 - (diff / 100);
                if(alpha > 1f) alpha = 1;
                sprite.setColor(r, g, b, alpha);
            }
        }

        sprite.translate(-Tile.SIZE / 2, -4);
        sprite.draw(batch);

        // Undo translations
        sprite.translate(Tile.SIZE / 2, 4);
        sprite.setColor(r, g, b, 1);
    }


}
