package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import java.util.ArrayList;
import java.util.Random;

public class Blueman extends Entity{

    private float speed = 0.5f;
    private float sprint = 4;
    private float[] directions = {speed, 0, -speed};
    private int moveCount, deadCount;
    boolean facingLeft, dead;

    // Footprints
    private Timer footprintTimer;

    // Animations
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private int currentAction = IDLE;

    private Animation animation;
    private ArrayList<Sprite[]> sprites = new ArrayList<Sprite[]>();
    private final int[] noFrames = {1, 6};

    private boolean attackReady = true;
    private boolean screamReady = true;
    private boolean approaching = false;
    protected Timer attackingTimer, screamingTimer;
    public Music steps;
    private Random r;


    public Blueman(float x, float y, float dx, float dy, float ddx, float ddy, String ref, TileMap map, MyScreen world) {

        super(x, y, dx, dy, ddx, ddy, ref, map, world);
        this.moveCount = 100;
        r = new Random();
        this.attackingTimer = new Timer(3000, false);
        this.screamingTimer = new Timer(5000, false);

        steps = Gdx.audio.newMusic(Gdx.files.internal("sfx/steps.wav"));
        steps.setVolume(0.4f);
        steps.setLooping(true);
        this.footprintTimer = new Timer(600, false);

        // Build spritesheet
        //
        // Idle
        Texture idleTex = TextureStore.get().getTexture("blueman");
        Sprite[] idleAnimation = new Sprite[noFrames[0]];
        idleAnimation[0] = new Sprite(idleTex);
        sprites.add(idleAnimation);

        // Walking & backwards walking
        //
        Texture walkingTex = TextureStore.get().getTexture("blueman_walkcycle");
        Sprite[] walkingAnimation = new Sprite[noFrames[1]];
        for(int i=0; i<noFrames[1]; i++) {
            walkingAnimation[i] = new Sprite(new TextureRegion(walkingTex, i * 12, 0, 12, 46));
        }
        sprites.add(walkingAnimation);

        // Set the default to 'idle'
        //
        this.animation = new Animation(sprites.get(1), 100);
    }

    Random random = new Random();

    public boolean tick(float dt) {

        moveCount ++;

        if(dead){
            vel.x = 0;
            vel.y = 0;
            return true;
        }
        else if(approaching){

            moveCount = 0;

            if (world.player.pos.x > this.pos.x) {
                if (vel.x > 0)
                    vel.x = -speed * sprint;
                else
                    vel.x = speed * sprint;
            }
            if (world.player.pos.x < this.pos.x)
                if (vel.x > 0)
                    vel.x = -speed * sprint;
                else
                    vel.x = speed * sprint;

            this.facingLeft = vel.x < 0;
            approaching = false;

        }
        else {
            if (moveCount > 100) {
                vel.x = directions[random.nextInt(3)];
                vel.y = directions[random.nextInt(3)];
                this.facingLeft = vel.x < 0;

                moveCount = 0;
            }
        }

        // Animation
        //
        // If moving, set the walk animation. Otherwise set it to idle.
        if(Math.abs(vel.x) > 0.1f || Math.abs(vel.y) > 0.1f) {
            if(currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                steps.play();
                steps.setLooping(true);
                footprintTimer.start();
            }
            float diff = (float) Math.sqrt(Math.pow(pos.x - world.player.pos.x, 2) + Math.pow(pos.y - world.player.pos.y, 2));
            if(diff > 200) {
                steps.setVolume(0);
            } else {
                steps.setVolume(1 - (diff / 200));
            }
        } else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(0));
                steps.setLooping(false);
                steps.stop();
                footprintTimer.stop();
            }
        }
        animation.tick(dt);
        animation.getCurrentSprite().setPosition(pos.x, pos.y);

        if(attackingTimer.tick(dt)) {
            attackReady = true;
            attackingTimer.stop();
        }

        if(screamingTimer.tick(dt)) {
            screamReady = true;
            screamingTimer.stop();
        }

        // Footprints
        //
        if(footprintTimer.tick(dt)) {
            float x_offset = 0;
            float y_offset = 0;
            if(Math.abs(vel.x) > 0) y_offset = (float) (Math.random() - 0.5) * 6;
            if(Math.abs(vel.y) > 0) x_offset = (float) (Math.random() - 0.5) * 10;
            world.addFootprint(new Footprint(pos.x + x_offset, pos.y + y_offset, map, world));
        }

        return super.tick(dt);
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        if(!dead) {
            // This is backwards because the sprite is drawn backwards
            if(facingLeft) {
                animation.getCurrentSprite().draw(batch);
            } else {
                animation.getCurrentSprite().flip(true, false);
                animation.getCurrentSprite().draw(batch);
                animation.getCurrentSprite().flip(true, false);
            }
        }

    }


    public void die(){

        if(!dead) {
            dead = true;
            deadCount = 0;
            // Large
            for(int i=0; i<5; i++) {
                float x = pos.x + width + (float) (Math.random() - 0.5) * 8;
                float y = pos.y + height / 2 + (float) (Math.random() - 0.5) * height / 4;
                float dx = (float) (Math.random() - 0.5) * 10;
                float dy = (float) (Math.random() - 0.1) * 10;
                float size = (float) (Math.random() * 5) + 10;
                world.addParticle(new Particle(x, y, dx, dy, 0, 0, 2, 2, size, new Color(34f/255f, 32f/255f, 52f/255f, 1)));
            }
            // Medium
            for(int i=0; i<10; i++) {
                float x = pos.x + width + (float) (Math.random() - 0.5) * 4;
                float y = pos.y + height / 2 + (float) (Math.random() - 0.5) * height;
                float dx = (float) (Math.random() - 0.5) * 25;
                float dy = (float) (Math.random() - 0.2) * 25;
                float size = (float) (Math.random() * 5) + 5;
                world.addParticle(new Particle(x, y, dx, dy, 0, 0, 2, 2, size, new Color(34f/255f, 32f/255f, 52f/255f, 1)));
            }
            // Small
            for(int i=0; i<10; i++) {
                float x = pos.x + width + (float) (Math.random() - 0.5) * 8;
                float y = pos.y + height / 2 + (float) (Math.random() - 0.5) * height;
                float dx = (float) (Math.random() - 0.5) * 25;
                float dy = (float) (Math.random() - 0.2) * 25;
                float size = (float) (Math.random() * 5);
                world.addParticle(new Particle(x, y, dx, dy, 0, 0, 2, 2, size, new Color(34f/255f, 32f/255f, 52f/255f, 1)));
            }

            world.main.scream.setVolume(0.5f);
            world.main.scream.play();
        }
    }

    public void attack(Player player){

        if(attackReady && !dead) {
            attackReady = false;
            attackingTimer.resetTimer();
            attackingTimer.start();
        }

    }

    public void scream(){

        if(screamReady && !dead) {

            world.main.scream.play();

            approaching = true;
            screamReady = false;
            screamingTimer.resetTimer();
            screamingTimer.start();

        }

    }

    public boolean isAttackReady(){

        return attackReady && !dead;
    }

    public boolean isScreamReady(){

        return screamReady && !dead;
    }

}
