package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Shell extends Entity {


    private float targetY;


    public Shell(float x, float y, float dx, float dy, float dtheta, float targetY, TileMap map, MyScreen world) {

        super(x, y, dx, dy, 0, 0, "shell", map, world);
        this.dtheta = dtheta;
        this.targetY = targetY;
    }


    public boolean tick(float dt) {


        if(pos.y <= targetY) {
            vel.x = 0;
            vel.y = 0;
            dtheta = 0;
        } else {
            vel.x += (0f - vel.x) * 2f * dt;
            vel.y += (-2f - vel.y) * 8f * dt;
        }
        boolean returnVal = super.tick(dt);
        return returnVal;
    }


    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        sprite.rotate((float)Math.toDegrees(theta));
        sprite.draw(batch);
        sprite.rotate(-(float)Math.toDegrees(theta));
    }


}
