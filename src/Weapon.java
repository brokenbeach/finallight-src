package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Weapon {

    protected MyScreen world;

    protected Vector2 pos;
    protected float theta;
    protected Sprite spriteLowered;
    protected Sprite spriteRaised;
    protected Player owner;

    // Attacking
    protected int ammo = 4;
    protected boolean raised = false;
    protected boolean readyToFire = true;
    protected Timer firingTimer;
    protected float kickback = 0f;

    public Weapon(float x, float y, float theta, String textureRef, Player owner, MyScreen world) {

        this.world = world;
        this.pos = new Vector2(x, y);
        this.theta = theta;
        this.spriteLowered = new Sprite(TextureStore.get().getTexture(textureRef + "_lowered"));
        this.spriteRaised = new Sprite(TextureStore.get().getTexture(textureRef + "_raised"));
        this.owner = owner;
        this.firingTimer = new Timer(2500, false);

    }


    public boolean tick(float dt) {

        // Position
        //
        pos.x = owner.pos.x;
        pos.y = owner.pos.y;

        if(owner.isFacingLeft()) {
            // Lowered
            spriteLowered.setPosition(pos.x - 10f, pos.y + 8f);

            // Raised
            spriteRaised.setPosition(pos.x, pos.y);
            spriteRaised.setCenter(pos.x -3f, pos.y + 19f - 4 * (MyScreen.getMousePos().y / (float) (Game.HEIGHT)));
        } else {
            // Lowered
            spriteLowered.setPosition(pos.x - 6f, pos.y + 8f);

            // Raised
            spriteRaised.setPosition(pos.x, pos.y);
            spriteRaised.setCenter(pos.x + 10f, pos.y + 19f - 2 * (MyScreen.getMousePos().y / (float) (Game.HEIGHT)));
        }

        // Rotation
        //
        if(raised) {
            if(owner.isFacingLeft()) {
                theta = (float) ((Math.PI / 4) + (MyScreen.getMousePos().y / (float) (Game.HEIGHT))) - 1.2f;
            } else {
                theta = (float) ((Math.PI / 4) - (MyScreen.getMousePos().y / (float) (Game.HEIGHT))) - 0.2f;
            }
            spriteRaised.setRotation((float)Math.toDegrees(theta));
        }

        // Adjust kickback
        //
        kickback = Math.abs(0f - kickback) * 0.6f;

        // Check firing timer
        //
        if(firingTimer.tick(dt)) {
            readyToFire = true;
            firingTimer.stop();
        }

        return false;
    }


    public void draw(SpriteBatch batch) {

        if(raised) {
            spriteRaised.translate(0f, 6f);
            if(owner.isFacingLeft()) {
                spriteRaised.translateX(kickback);
                spriteRaised.flip(true, false);
                spriteRaised.draw(batch);
                spriteRaised.flip(true, false);
            } else {
                spriteRaised.translateX(6f - kickback);
                spriteRaised.draw(batch);
            }
        } else {
            if(owner.isFacingLeft()) {
                spriteLowered.translateX(kickback);
                spriteLowered.flip(true, false);
                spriteLowered.draw(batch);
                spriteLowered.flip(true, false);
            } else {
                spriteLowered.translateX(6f - kickback);
                spriteLowered.draw(batch);
            }
        }
        
    }


    public void fire(Vector2 lightPos) {

        if(raised && readyToFire && ammo > 0) {

            // Play sound
            world.main.shot.setVolume(0.2f);
            world.main.shot.stop();
            world.main.shot.play();
            world.main.explosion.stop();
            world.main.explosion.setVolume(0.5f);
            world.main.explosion.play();

            // Fire
            ammo--;
            unloadShell();
            world.addLight(new TimedLight(lightPos.x, lightPos.y, 100, 130));
            kickback = 8f;
            world.rumble(4, 8);

            // Disable firing for a short time
            readyToFire = false;
            firingTimer.resetTimer();
            firingTimer.start();
            world.main.reload.play();
        }
        else{
            world.main.click.stop();
            world.main.click.setVolume(0.4f);
            world.main.click.play();
        }
    }


    private void unloadShell() {

        int dir = (owner.isFacingLeft()) ? 1 : -1;
        float dx = (float) (dir * (1 + Math.random()));
        float dy = (float) (Math.random() * 3) + 4f;
        float dtheta = (float) (-dir * 0.5f);
        world.addShell(new Shell(pos.x, pos.y + 20f, dx, dy, dtheta, pos.y, owner.map, world));
    }


    public boolean isRaised() {

        return this.raised;
    }


    public boolean isReadyToFire() {

        return readyToFire;
    }


    public int getAmmo(){

        return this.ammo;
    }

    public void addAmmo(int i){

        ammo = ammo + i;

        if(ammo > 10)
            ammo = 10;

    }


}
