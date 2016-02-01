package com.tedigc.ggj;

public class Son extends Entity {


    public Son(TileMap map, MyScreen world) {
        super(map.getWidth() / 2 * Tile.SIZE +30, map.getHeight() / 2 * Tile.SIZE,0, 0 ,0 ,0 , "son", map, world);
    }


}
