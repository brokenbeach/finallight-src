package com.tedigc.ggj;

import java.util.Random;

public class Rumble {

    public float time;
    Random random;
    float x, y;
    float current_time;
    float power;
    float current_power;

    public Rumble(){
        time = 0;
        current_time = 0;
        power = 0;
        current_power = 0;
    }

    // Call this function with the force of the shake
    // and how long it should last
    public void rumble(float power, float time) {
        random = new Random();
        this.power = power;
        this.time = time;
        this.current_time = 0;
    }

    public void tick(float delta, MyScreen gc, Player hero){
        // GameController contains the camera
        // Hero is the character centre screen

        if(current_time <= time) {
            current_power = power * ((time - current_time) / time);
            // generate random new x and y values taking into account
            // how much force was passed in
            x = (random.nextFloat() - 0.5f) * 2 * current_power;
            y = (random.nextFloat() - 0.5f) * 2 * current_power;

            // Set the camera to this new x/y position
            gc.camera.translate(-x, -y);
            current_time += delta;
        } else {
//            // When the shaking is over move the camera back to the hero position
//            gc.camera.position.x = hero.pos.x;
//            gc.camera.position.y = hero.pos.y;
        }
    }
}