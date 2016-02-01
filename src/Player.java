package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Player extends Entity {


    private Light light;
    private Weapon weapon;
    private int health = 3;
    private int woodCount = 0;

    // Timers
    private Timer footprintTimer;
    private Timer bloodTimer;

    // Animations
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int WALKING_BACKWARDS = 2;
    private int currentAction = IDLE;

    private Animation animation;
    private ArrayList<Sprite[]> sprites = new ArrayList<Sprite[]>();
    private final int[] noFrames = {1, 6};
    public Music steps;

    // Speech
    private boolean isSpeaking = false;
    private String text;
    private Timer speechTimer;
    private GlyphLayout layout;
    private BitmapFont font;


    public Player(TileMap map, MyScreen world) {

        super(map.getWidth() / 2 * Tile.SIZE - 30, map.getHeight() / 2 * Tile.SIZE,0, 0 ,0 ,0 , "player", map, world);
        light = new Light(map.getWidth() / 2 * Tile.SIZE, -570, 100, false);
        world.addLight(light);

        steps = Gdx.audio.newMusic(Gdx.files.internal("sfx/steps.wav"));
        steps.setVolume(0.4f);
        steps.setLooping(true);

        weapon = new Weapon(200, 200, 0, "rifle", this, world);

        this.footprintTimer = new Timer(200, false);
        this.bloodTimer = new Timer(350, false);

        // Build spritesheet
        //
        // Idle
        Texture idleTex = TextureStore.get().getTexture("player");
        Sprite[] idleAnimation = new Sprite[noFrames[0]];
        idleAnimation[0] = new Sprite(idleTex);
        sprites.add(idleAnimation);

        // Walking & backwards walking
        Texture walkingTex = TextureStore.get().getTexture("player_walkcycle");
        Texture backwardsTex = TextureStore.get().getTexture("player_walkcycle_backwards");
        Sprite[] walkingAnimation = new Sprite[noFrames[1]];
        Sprite[] backwardsAnimation = new Sprite[noFrames[1]];
        for(int i=0; i<noFrames[1]; i++) {
            walkingAnimation[i] = new Sprite(new TextureRegion(walkingTex, i * 12, 0, 12, 34));
            backwardsAnimation[i] = new Sprite(new TextureRegion(backwardsTex, i * 12, 0, 12, 34));
        }
        sprites.add(walkingAnimation);
        sprites.add(backwardsAnimation);

        // Set the default to 'idle'
        //
        this.animation = new Animation(sprites.get(0), 100);

        // Speech
        //
        this.text = "";
        this.speechTimer = new Timer(0, false);
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.layout.setText(font, "");
    }


    public boolean tick(float dt) {

        // Check direction the player is facing
        //
        if(weapon.isRaised()) {
            facingLeft = MyScreen.getMousePos().x < Game.WIDTH / 2;
            footprintTimer.setDelay(400);
        } else {
            footprintTimer.setDelay(200);
            if(vel.x < 0) {
                facingLeft = true;
            } else {
                if(vel.x > 0) {
                    facingLeft = false;
                }
            }
        }
        // Player movement
        //
        float speed = (weapon.isRaised() || health == 1) ? 20f : 50f;
        if(left && right) {
            vel.x = (Math.abs(vel.x) > 0.2f) ? (0 - vel.x) * 0.2f : 0f;
        } else if(left){
            vel.x = -speed * dt;
        } else if(right){
            vel.x = speed * dt;
        } else {
            vel.x = 0;
        }

        if(up && down) {
            vel.y = (0 - vel.y) * 0.5f;
        } else if(up){
            vel.y = speed * dt;
        } else if(down){
            vel.y = -speed * dt;
        } else {
            vel.y = (0 - vel.y) * 0.5f;
        }

        boolean returnVal = super.tick(dt);
        weapon.tick(dt);
        updateLightPosition(dt);

        // Animation
        //
        // If moving, set the walk animation. Otherwise set it to idle.
        if(Math.abs(vel.x) > 0.1f || Math.abs(vel.y) > 0.1f) {
            steps.play();
            // Is the player walking backwards?
            if(Math.abs(vel.x) > 0.1f) {
                if(facingLeft && vel.x > 0 || !facingLeft && vel.x < 0) {
                    if(currentAction != WALKING_BACKWARDS) {
                        currentAction = WALKING_BACKWARDS;
                        animation.setFrames(sprites.get(WALKING_BACKWARDS));
                        footprintTimer.start();
                        steps.play();
                    }
                } else {
                    if(currentAction != WALKING) {
                        currentAction = WALKING;
                        animation.setFrames(sprites.get(WALKING));
                        footprintTimer.start();
                        steps.play();
                    }
                }
            } else {
                if(currentAction != WALKING) {
                    currentAction = WALKING;
                    animation.setFrames(sprites.get(WALKING));
                    footprintTimer.start();
                    steps.play();
                }
            }
            animation.setDelay(weapon.isRaised() ? 150 : 100);
        } else {
            steps.setLooping(false);
            steps.stop();
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(0));
                footprintTimer.stop();
            }
        }

        animation.tick(dt);
        animation.getCurrentSprite().setPosition(pos.x, pos.y);

        // Footprints
        //
        if(footprintTimer.tick(dt)) {
            float x_offset = 0;
            float y_offset = 0;
            if(Math.abs(vel.x) > 0) y_offset = (float) (Math.random() - 0.5) * 6;
            if(Math.abs(vel.y) > 0) x_offset = (float) (Math.random() - 0.5) * 10;
            world.addFootprint(new Footprint(pos.x + x_offset, pos.y + y_offset, map, world));
        }

        if(bloodTimer.tick(dt)) {
            String bloodRef = "";
            double rand = Math.random();
            if(rand > 0.9) {
                bloodRef = "blood0";
            } else if(rand > 0.4) {
                bloodRef = "blood1";
            } else  {
                bloodRef = "blood2";
            }
            float x_offset = 0;
            float y_offset = 0;
            if(Math.abs(vel.x) > 0) y_offset = (float) (Math.random() - 0.5) * 6;
            if(Math.abs(vel.y) > 0) x_offset = (float) (Math.random() - 0.5) * 10;
            world.addBlood(new Blood(pos.x + x_offset, pos.y + y_offset, bloodRef, map, world));
        }

        if(health != 1) {
            bloodTimer.resetTimer();
            bloodTimer.stop();
        }

        if(isSpeaking && speechTimer.tick(dt)) {
            isSpeaking = false;
            text = "";
        }

        int tileX = (int) (pos.x / Tile.SIZE);
        int tileY = (int) (pos.y / Tile.SIZE);
        if(tileX < 5 || tileX > map.getWidth() - 5 ||
                tileY < 5 || tileY > map.getHeight() - 5) {
            say("I should probably turn back...", 2000);
        }

        return returnVal;
    }


    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        if(health > 0) {
            if(facingLeft) {
                animation.getCurrentSprite().flip(true, false);
                animation.getCurrentSprite().draw(batch);
                animation.getCurrentSprite().flip(true, false);
            } else {
                animation.getCurrentSprite().draw(batch);
            }
            weapon.draw(batch);
        }
    }


    public void drawSpeech(SpriteBatch batch) {

        if(isSpeaking()) {
            font.draw(batch, text, pos.x - (layout.width / 2), pos.y + 60);
        }
    }


    // Method for the camera 'tween aiming' effect
    //
    private void updateLightPosition(float dt) {

        float scaling = 0.1f;
        float tweening = 0.08f;

        float x_lightTarget = pos.x + width / 2;
        float y_lightTarget = (Game.HEIGHT - pos.y) - height / 2;
        if(weapon.isRaised()) {
            float x_mouseOffset = (Game.WIDTH / 2 - world.getMousePos().x) * scaling;
            float y_mouseOffset = (Game.HEIGHT / 2 - world.getMousePos().y) * scaling;
            x_lightTarget -= x_mouseOffset;
            y_lightTarget -= y_mouseOffset;
        }

        light.pos.add(
                (x_lightTarget - light.pos.x) * tweening,
                (y_lightTarget - light.pos.y) * tweening
        );
    }


    public void attack() {

        if(weapon != null) {
            weapon.fire(light.pos);
        }
    }


    public void raiseWeapon() {

        weapon.raised = true;
    }


    public void lowerWeapon() {

        weapon.raised = false;
    }

    public boolean isWeaponRaised() {

        return weapon.raised;
    }

    public boolean isWeaponReady(){

        return weapon.isReadyToFire() && weapon.isRaised() && weapon.getAmmo() > 0;
    }

    public boolean withinRange(Entity entity){


        Rectangle range = null;

        if(facingLeft)
            range = new Rectangle(
                    this.getRectangle().x - this.getRectangle().getHeight()*3,
                    this.getRectangle().y - this.getRectangle().getHeight(),
                    this.getRectangle().getHeight()*3,
                    this.getRectangle().getHeight()*3
            );
        else
            range = new Rectangle(
                    this.getRectangle().x,
                    this.getRectangle().y - this.getRectangle().getHeight(),
                    this.getRectangle().getHeight()*3,
                    this.getRectangle().getHeight()*3
            );

        return range.overlaps(entity.getRectangle());
    }

    public int getHealth(){

        return this.health;
    }

    public void decreaseHealth(int i){

        health = health - i;
    }

    public void increaseHealth(int i){

        health = 3;
    }

    public void increaseAmmo(int i){

        weapon.addAmmo(i);
    }

    public void hitBy(Entity entity){

        world.main.grunt.stop();
        world.main.grunt.play();

        if(entity.pos.x < pos.x)
            pos.x += 20;
        if(entity.pos.x > pos.x)
            pos.x -= 20;

        // Small
        for(int i=0; i<20; i++) {
            float x = pos.x + width + (float) (Math.random() - 0.5) * 8;
            float y = pos.y + height / 2 + (float) (Math.random() - 0.5) * height;
            float dx = (float) (Math.random() - 0.5) * 50;
            float dy = (float) (Math.random() - 0.2) * 50;
            float size = (float) (Math.random() * 3);
            world.addParticle(new Particle(x, y, dx, dy, 1, 1, 2, 1, size, new Color(238f/255f, 0/255f, 0/255f, 1)));
        }
        health--;
        if(health == 1) {
            bloodTimer.resetTimer();
            bloodTimer.start();
        }
        if(health <= 0) {
            world.lose(0);
            world.main.scream.play();
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
        }
    }


    public void say(String text, float duration) {

        this.text = text;
        this.isSpeaking = true;
        this.speechTimer.resetTimer();
        this.speechTimer.setDelay(duration);
        this.speechTimer.start();
        this.layout.setText(font, text);
    }


    public int getWoodCount(){

        return this.woodCount;
    }


    public void increaseWood(int i){

        woodCount = woodCount + i;
    }


    public String getText() {

        return this.text;
    }


    public boolean isSpeaking() {

        return this.isSpeaking;
    }



}