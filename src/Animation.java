package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Animation {


    private Sprite[] frames;
    private int currentFrame;
    private boolean playedOnce;
    public Timer timer = new Timer(0);


    public Animation(Sprite[] frames, float delay) {

        this.frames = frames;
        this.timer  = new Timer(delay, true);
    }


    public void setFrames(Sprite[] frames) {

        this.frames = frames;
        currentFrame = 0;
        playedOnce = false;
        timer.resetTimer();
        timer.start();
    }

    public void tick(float dt) {

        // Check if it's time to change frame
        if(timer.tick(dt)) {
            this.currentFrame++;
        }
        // If at the end, loop back to the first frame.
        if(currentFrame >= frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public void setDelay(float delay) {

        timer.setDelay(delay);
    }


    public void setFrame(int i) {

        this.currentFrame = i;
    }


    public int getCurrentFrame() {

        return this.currentFrame;
    }


    public int getNFrames() {

        return this.frames.length;
    }


    public Sprite getCurrentSprite() {

        return frames[currentFrame];
    }


    public boolean hasPlayedOnce() {

        return this.playedOnce;
    }


}