package com.tedigc.ggj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TileMap {


    private int width;
    private int height;
    private int[][] solidarityMap;
    private int[][] typeMap;
    private Tile[][] tileMap;


    public TileMap(final int width, final int height, MyScreen world) {

        this.width = width;
        this.height = height;

        this.solidarityMap = generateBlankMap(width, height);
        // Add trees
        for(int i=0; i<400; i++) {
            boolean ok = false;
            while(!ok) {
                int x = (int) (Math.random() * width-1) + 1;
                int y = (int) (Math.random() * height-1) + 1;
                int houseWidth = 10;
                int houseHeight = 8;
                if(x > (width - houseWidth) / 2 && x < (width + houseWidth) / 2 &&
                        y > (height - houseHeight) / 2 && y < (height + houseHeight) / 2) continue;
                ok = true;
                solidarityMap[y][x] = 1;
                double rand = Math.random();
                String textureRef = "";
                if(rand > 0.9) {
                    textureRef = "treestump";
                } else if(rand > 0.66) {
                    textureRef = "tree0";
                } else if(rand > 0.33) {
                    textureRef = "tree1";
                } else {
                    textureRef = "tree2";
                }
                world.addTree(new Tree(x * Tile.SIZE, y * Tile.SIZE, textureRef, this, world));
            }

        }

        this.typeMap = copyArray(solidarityMap);

        tileMap = new Tile[height][width];
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                tileMap[y][x] = new Tile(x, y, typeMap[y][x]);
            }
        }
    }


    private int[][] generateBlankMap(int width, int height) {

        int[][] map = new int[height][width];
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                if(x == 0 || x == width-1 ||
                   y == 0 || y == height-1) {
                    map[y][x] = 1;
                }
            }
        }
        return map;
    }


    public void tick(float dt) {

        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                tileMap[y][x].tick(dt);
            }
        }
    }


    public void draw(SpriteBatch batch) {

        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                tileMap[y][x].draw(batch);
            }
        }
    }


    public boolean isBlocked(int x, int y) {

        return solidarityMap[y][x] == 1;
    }


    public Tile getTile(int x, int y) {

        return this.tileMap[y][x];
    }


    private int[][] copyArray(int[][] original) {

        int[][] copy = new int[original.length][original[0].length];
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                copy[y][x] = original[y][x];
            }
        }
        return copy;
    }


    private void printMap(int[][] map) {

        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                System.out.print(map[y][x] + " ");
            }
            System.out.println();
        }
    }


    public Tile[][] getTileMap() {

        return tileMap;
    }


    public int getHeight() {

        return height;
    }


    public int getWidth() {

        return width;
    }



}