package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity implements Comparable<Entity> {

    protected MyScreen world;

    // Entity properties
    protected Vector2 pos;
    protected Vector2 vel;
    protected Vector2 acc;
    protected Vector2 rot;
    protected float theta  = 0.0f;
    protected float dtheta = 0.0f;
    protected Sprite sprite;
    protected float width;
    protected float height;
    protected Rectangle hitbox;
    protected boolean facingLeft;


    // Movement bools
    protected boolean up;
    protected boolean down;
    protected boolean left;
    protected boolean right;

    // Map and collision
    protected TileMap map;
    private boolean topLeft;
    private boolean topRight;
    private boolean bottomLeft;
    private boolean bottomRight;


    public Entity(float x, float y, float dx, float dy, float ddx, float ddy, String ref, TileMap map, MyScreen world) {

        this.world = world;
        this.pos = new Vector2(x, y);
        this.vel = new Vector2(dx, dy);
        this.acc = new Vector2(ddx, ddy);
        this.rot = new Vector2(0.5f, 0.5f);
        this.sprite = new Sprite(TextureStore.get().getTexture(ref));
        this.sprite.setPosition(x, y);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.hitbox = new Rectangle(x, y, 12, 12);
        this.map = map;
    }


    public boolean tick(float dt) {

        theta += dtheta;
        vel.add(acc);
        checkMapCollision();
        updateHitbox();
        return false;
    }


    public void draw(SpriteBatch batch, ShapeRenderer renderer) {

        if(facingLeft) {
            sprite.flip(true, false);
            sprite.draw(batch);
            sprite.flip(true, false);
        } else {
            sprite.draw(batch);
        }
    }


    public Rectangle getRectangle(){

        return new Rectangle(pos.x, pos.y, width, height);
    }


    public boolean collides(Entity entity){

        return this.getRectangle().overlaps(entity.getRectangle());
    }


    protected void checkMapCollision() {

        // Check horizontal movement
        checkCorners(hitbox.x + vel.x, hitbox.y);
        if(vel.x < 0) {
            if(topLeft || bottomLeft) {
                vel.x = 0;
            }
        } else if(vel.x > 0) {
            if(topRight || bottomRight) {
                vel.x = 0;
            }
        }

        // Check vertical movement
        checkCorners(hitbox.x, hitbox.y + vel.y);
        if(vel.y > 0) {
            if(topLeft || topRight) {
                vel.y = 0;
            }
        } else if(vel.y < 0) {
            if(bottomLeft || bottomRight) {
                vel.y = 0;
            }
        }
        pos.add(vel);
        sprite.setPosition(pos.x, pos.y);
    }


    protected void checkCorners(double x, double y) {

        int left   = (int)  x / Tile.SIZE;
        int right  = (int) (x + hitbox.width) / Tile.SIZE;
        int bottom = (int)  y / Tile.SIZE;
        int top    = (int) (y + hitbox.height) / Tile.SIZE;

        this.topLeft = map.isBlocked(left, top);
        this.topRight = map.isBlocked(right, top);
        this.bottomLeft = map.isBlocked(left, bottom);
        this.bottomRight = map.isBlocked(right, bottom);
    }


    protected void updateHitbox() {

        this.hitbox.set(pos.x, pos.y, 10, 10);
    }
    
    
    public void setLeft(boolean left) {
        
        this.left = left;
    }


    public void setRight(boolean right) {

        this.right = right;
    }


    public void setUp(boolean up) {

        this.up = up;
    }


    public void setDown(boolean down) {

        this.down = down;
    }


    public boolean isFacingLeft() {

        return this.facingLeft;
    }

    public void updateSprite(String ref){

        this.sprite = new Sprite(TextureStore.get().getTexture(ref));
    }

    @Override
    public int compareTo(Entity o) {
        if(o.pos.y > pos.y) {
            return 1;
        } else if(o.pos.y < pos.y) {
            return -1;
        }
        return 0;
    }


}