package com.tedigc.ggj;

/*
 *   - Note:
 *  This should eventually be replaced by some more sophisticated
 *  method of asset management (the libGDX "AssetManager" class
 *  for example.
 *
 *  - tedigc
 */

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class TextureStore {


    private static TextureStore store = new TextureStore();
    private Map<String, Texture> reference = new HashMap<String, Texture>();


    public TextureStore() {

        // Load ALL Textures here.
        load("test0", "test/test0.png");
        load("test1", "test/test1.png");
        load("test2", "test/test2.png");

        // Mobs
        load("test_player", "mobs/test_player.png");
        load("player", "mobs/player.png");
        load("player_walkcycle", "mobs/player_walkcycle.png");
        load("player_walkcycle_backwards", "mobs/player_walkcycle_backwards.png");
        load("enderman", "mobs/enderman2.png");
        load("blueman", "mobs/enderman.png");
        load("blueman_walkcycle", "mobs/blueman_walkcycle.png");
        load("smoke", "mobs/smoke2.png");
        load("smoke2", "mobs/smoke.png");
        load("son", "mobs/son.png");

        // Weapons
        load("rifle", "weapons/rifle.png");
        load("rifle_raised", "weapons/rifle_raised.png");
        load("rifle_lowered", "weapons/rifle_lowered.png");

        // Environment
        load("treestump", "environment/treestump.png");
        load("tree0", "environment/tree0.png");
        load("tree1", "environment/tree1.png");
        load("tree2", "environment/tree2.png");
        load("wood", "environment/wood.png");
        load("ammo", "environment/ammo.png");
        load("healthkit", "environment/healthkit.png");


        // Tiles
        load("tile_test0", "tiles/testtile0.png");
        load("tile_test1", "tiles/testtile1.png");
        load("tile_snow0", "tiles/tile_snow0.png");

        // Misc
        load("firewood", "firewood.png");
        load("shell", "shell.png");
        load("footprint", "footprint.png");
        load("white", "weapons/white.png");
        load("black", "weapons/black.png");
        load("title", "title.png");
        load("blood0", "blood0.png");
        load("blood1", "blood1.png");
        load("blood2", "blood2.png");
        load("youlose", "youlose.png");
        load("boylose", "boylose.png");
    }


    private void load(String ref, String url) {

        this.reference.put(ref, new Texture(url));
    }


    public static TextureStore get() {

        return store;
    }


    public Texture getTexture(String ref) {

        return this.reference.get(ref);
    }


}
