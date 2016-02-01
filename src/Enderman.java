package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * Created by marcosss3 on 29/1/16.
 */
public class Enderman extends Entity{

    public static final int spawnProbability = 200;
    private float speed = 1;
    private float[] directions = {speed, 0, -speed};
    private int moveCount, moveLimit;
    boolean facingLeft, facingRight, reverse;
    private Sound scream;

    public Enderman(float x, float y, float dx, float dy, float ddx, float ddy, String ref, TileMap map, MyScreen world) {

        super(x, y, dx, dy, ddx, ddy, ref, map, world);
        this.moveCount = 100;
        this.moveLimit = 100;
    }

    Random random = new Random();

    public boolean tick(float dt) {

        moveCount ++;

        if(reverse == true){
            vel.x = -vel.x*2;
            vel.y = -vel.y*2;
            sprite.flip(true, false);
            moveCount = 0;
            reverse = false;
        }

        if(moveCount > 100) {

            vel.x = directions[random.nextInt(3)];
            vel.y = directions[random.nextInt(3)];

            facingLeft = vel.x == -speed;
            facingRight = vel.x == speed;

            // Facing Left
            if(facingLeft) {
                sprite.setFlip(true, false);
            }
            // Facing Right
            if(facingRight) {
                sprite.setFlip(false, false);
            }

            moveCount = 0;
            moveLimit = random.nextInt(300 - 50) + 50;

        }

        return super.tick(dt);
    }

    public void reverseDirection(){

        reverse = true;
        scream = Gdx.audio.newSound(Gdx.files.internal("sfx/spooky.wav"));
        scream.play(0.9f);
    }

}
