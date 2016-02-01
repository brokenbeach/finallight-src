package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MyScreen implements Screen, InputProcessor {


    protected Game main;
    public static OrthographicCamera camera;
    private static Viewport viewport;
    private static Vector2 mousePos;
    private Random r;

    // Fade in/out
    private float alpha = 1f;
    private boolean fadingIn = true;
    private boolean fadingOut = false;
    private Timer inTimer;
    private Timer outTimer;

    // Map
    private TileMap map;

    // Speech
    private GlyphLayout layout;
    private BitmapFont font;

    // Entities
    public  Player player;
    public  Son son;
    public  FirePlace fireplace;
    private List<Footprint> footprints;
    private List<Shell> shells;
    private List<Blood> blood;
    private List<Tree> trees;
    private List<Blueman> bluemen;
    private List<Particle> particles;
    private List<Entity> allEntities;
    private List<Wood> woodList;
    private List<AmmoPack> ammoList;
    private List<HealthKit> healthKitList;
    private Timer spawnTimer;
    private static int woodChance = 2;
    private static int ammoChance = 3;
    private static int bluemanChance = 5;

    private int loseCase = 0;

    // Lights
    private List<Light> lights;

    Rumble rumble = new Rumble();

    public MyScreen(Game main) {

        this.main = main;
        this.camera = new OrthographicCamera();
        this.camera.position.set(180 * Tile.SIZE / 2,  140 * Tile.SIZE / 2, 0);
        this.viewport = new FitViewport(main.WIDTH, main.HEIGHT, camera);
        this.r = new Random();
        Gdx.input.setInputProcessor(this);

        inTimer = new Timer(500, true);
        outTimer = new Timer(5000, false);
        spawnTimer = new Timer(20000, true);

        if(main.intro != null) main.intro.play();

        init();
    }


    private void init() {

        this.mousePos = new Vector2();

        // List
        this.lights      = new ArrayList<Light>();
        this.footprints  = new ArrayList<Footprint>();
        this.shells      = new ArrayList<Shell>();
        this.blood       = new ArrayList<Blood>();
        this.trees       = new ArrayList<Tree>();
        this.bluemen     = new ArrayList<Blueman>();
        this.allEntities = new ArrayList<Entity>();
        this.particles   = new ArrayList<Particle>();
        this.woodList    = new ArrayList<Wood>();
        this.ammoList    = new ArrayList<AmmoPack>();
        this.healthKitList    = new ArrayList<HealthKit>();

        // Speech
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.layout.setText(font, "");

        // Map
        this.map = new TileMap(180, 140, this);

        // Entities
        this.player    = new Player(map, this);
        this.son       = new Son(map, this);
        this.fireplace = new FirePlace((map.getWidth() * Tile.SIZE) / 2, (map.getHeight() * Tile.SIZE) / 2, map, this);

        allEntities.add(player);
        allEntities.add(son);
        allEntities.add(fireplace);


        for(int i=0; i<100; i++) {
            addBlueman();
        }

        for(int i=0; i<50; i++) {
            addWood();
        }

        for(int i=0; i<5; i++) {
            addAmmo();
        }

        for(int i=0; i<5; i++) {
            addHealthKits();
        }


        main.fire.play();
        main.fire.setLooping(true);
    }

    // TICK
    //
    private void tick(float dt) {

        if(fadingIn) {
            alpha -= dt * 0.2f;
            if(alpha <= 0.5f) {
                say("Stay close to the fire son. I'll be back soon...", 2500);
            }
            if(alpha <= 0) {
                fadingIn = false;
            }
        }

        if(fadingOut) {
            alpha += dt * 0.5f;
            if(alpha >= 1) {
                for(int i=0; i<bluemen.size(); i++) {
                    bluemen.get(i).steps.stop();
                    bluemen.remove(i);
                }
                System.out.println("hi");
                player.steps.stop();
                main.setScreen(new Outro(main, loseCase));
            }
        }

        updateCamera(dt);

        if(spawnTimer.tick(dt)){

            addBlueman();
            addWood();
            addWood();

        }

        map.tick(dt);
        player.tick(dt);

        // Fire volume
        //
        float diff = (float) Math.sqrt(Math.pow(fireplace.pos.x - player.pos.x, 2) + Math.pow(fireplace.pos.y - player.pos.y, 2));
        if(diff > 400) {
            main.fire.setVolume(0);
        } else {
            float volume = (1 - (diff / 400)) - 0.7f;
            if(volume < 0f) volume = 0f;
            main.fire.setVolume(volume);
        }

        if(fireplace.tick(dt)) {
            lights.remove(fireplace.getLight());
        }
        
        // Tick Bluemen
        //
        for(int i=0; i<bluemen.size(); i++) {
            Blueman blueman = bluemen.get(i);

            if(player.collides(blueman)) {
                if(blueman.isAttackReady()){
                    rumble.rumble(8, 8);
                    player.hitBy(blueman);
                    blueman.attack(player);
                }
            }

            if(player.withinRange(blueman))
                if(blueman.isScreamReady())
                    blueman.scream();

            if(blueman.tick(dt)) {
                bluemen.remove(i);
                allEntities.remove(blueman);
                if(i > 0) i--;
            }
        }

        // Tick Wood
        //
        for(int i=0; i<woodList.size(); i++) {
            Wood wood = woodList.get(i);

            if(player.collides(wood)) {
                if(!wood.isCollected()){
                    wood.pickUp();
                    player.increaseWood(1);
                }
            }

            if(wood.tick(dt)) {
                bluemen.remove(i);
                allEntities.remove(wood);
                if(i > 0) i--;
            }
        }

        // Tick Ammo
        //
        for(int i=0; i<ammoList.size(); i++) {
            AmmoPack ammo = ammoList.get(i);

            if(player.collides(ammo)) {
                if(!ammo.isCollected()){
                    ammo.pickUp();
                    player.increaseAmmo(r.nextInt(3)+1);
                }
            }

            if(ammo.tick(dt)) {
                ammoList.remove(i);
                allEntities.remove(ammo);
                if(i > 0) i--;
            }
        }

        // Tick Health Kits
        //
        for(int i=0; i<healthKitList.size(); i++) {
            HealthKit healthKit = healthKitList.get(i);

            if(player.collides(healthKit)) {
                if(!healthKit.isCollected()){
                    healthKit.pickUp();
                    player.increaseHealth(1);
                }
            }

            if(healthKit.tick(dt)) {
                healthKitList.remove(i);
                allEntities.remove(healthKit);
                if(i > 0) i--;
            }
        }

        // Tick lights
        //
        for(int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            if(light.tick(dt)) {
                lights.remove(i);
                if(i > 0) i--;
            }
        }

        tickList(footprints, dt);
        tickList(shells, dt);
        tickList(blood, dt);

        // Tick particles
        //
        for(int i=0; i<particles.size(); i++) {
            Particle particle = particles.get(i);
            if(particle.tick(dt)) {
                particles.remove(i);
                if(i > 0) i--;
            }
        }

        // Screenshake
        //
        if (this.rumble.time > 0){
            this.rumble.tick(0.2f, this, player);
        }

        // Sort all entities
        Collections.sort(allEntities);
    }


    private void tickList(List<? extends Entity> list, float dt) {

        for(int i=0; i<list.size(); i++) {
            Entity entity = list.get(i);
            if(entity.tick(dt)) {
                list.remove(i);
                allEntities.remove(entity);
                if(i > 0) i--;
            }
        }
    }


    public void lose(int loseCase) {

        fadingOut = true;
    }


    // DRAW
    //
    private void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        Gdx.gl.glClearColor(203f/255f, 219f/255f, 252f/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(int i=0; i<particles.size(); i++) {
            particles.get(i).draw(shapeRenderer);
        }

        for(Entity entity : allEntities) {
            entity.draw(batch, shapeRenderer);
        }

        batch.end();

        shapeRenderer.end();

        main.lightbatch.begin();
            drawLighting(main.lightbatch);
        main.lightbatch.end();

        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        main.fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        main.fadeRenderer.setColor(0, 0, 0, alpha);
        main.fadeRenderer.rect(0, 0, Game.WIDTH, Game.HEIGHT);
        main.fadeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.drawSpeech(batch);
        batch.end();
    }


    private void drawLighting(SpriteBatch batch) {

        // Create dark rectangle covering everything
        Pixmap overlay = new Pixmap(main.WIDTH, main.HEIGHT, Pixmap.Format.RGBA8888);
        overlay.setColor(0, 0, 0, 1f);
        overlay.fillRectangle(0, 0, main.WIDTH, main.HEIGHT);
        overlay.setBlending(Pixmap.Blending.None);

        // Draw the dark, outer circle
        overlay.setColor(0, 0, 0, 0.8f);

        for(int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            float x = (light.getX() - camera.position.x) + Game.WIDTH / 2;
            float y = (light.getY() + camera.position.y) - Game.HEIGHT / 2;
            overlay.fillCircle((int) x, (int) y, (int) light.getRadius());
        }

        // Draw the bright, inner circle
        overlay.setColor(0, 0, 0, 0.3f);
        for(int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            if(light.isBright()) {
                float x = (light.getX() - camera.position.x) + Game.WIDTH / 2;
                float y = (light.getY() + camera.position.y) - Game.HEIGHT / 2;
                overlay.fillCircle((int) x, (int) y, (int) light.getRadius() - 20);
            }
        }

        overlay.setBlending(Pixmap.Blending.SourceOver);

        // Turn it into a texture
        Texture lighting = new Texture(overlay);
        overlay.dispose();
        batch.draw(lighting, 0, 0);
    }


    // Method for the camera 'tween aiming' effect
    //
    private void updateCamera(float dt) {

        float scaling = (player.isWeaponRaised()) ? 0.3f : 0.1f;
        float tweening = 0.05f;

        // Offset between player and mouse
        float x_mouseOffset = (Game.WIDTH / 2 - mousePos.x) * scaling;
        float y_mouseOffset = (Game.HEIGHT / 2 - mousePos.y) * scaling;

        // Where the camera wants to go
        float x_cameraTarget = player.pos.x - x_mouseOffset;
        float y_cameraTarget = player.pos.y + y_mouseOffset;

        camera.position.add(
                (x_cameraTarget - camera.position.x) * tweening,
                (y_cameraTarget - camera.position.y) * tweening,
                0
            );

        camera.update();
    }


    public void say(String text, float duration) {

        player.say(text, duration);
        this.layout.setText(font, text);
    }


    public void rumble(int power, int time) {

        this.rumble.rumble(power, time);
    }


    public void addLight(Light light) {

        this.lights.add(light);
    }


    public void addParticle(Particle particle) {

        this.particles.add(particle);
    }


    public void addTree(Tree tree) {

        this.trees.add(tree);
        this.allEntities.add(tree);
    }

    public void addBlueman(){

        Blueman blueman = null;

        boolean colliding = true;

        // Make sure enderman does not spawn on the player
        while(colliding){
            float x = (float) ((Math.random() * (map.getWidth()-1))) * Tile.SIZE;
            float y = (float) ((Math.random() * (map.getHeight()-1))) * Tile.SIZE;
            float x_diff = Math.abs(x - player.pos.x);
            float y_diff = Math.abs(y - player.pos.y);
            float mag = (float) Math.sqrt(x_diff*x_diff + y_diff*y_diff);
            if(mag > 300) {
                blueman = new Blueman(x, y, 0, 0, 0, 0, "blueman", map, this);
                colliding = false;
            }
        }

        bluemen.add(blueman);
        allEntities.add(blueman);
    }

    public void addWood(){

        Wood wood = null;

        boolean colliding = true;

        // Make sure enderman does not spawn on the player
        while(colliding){
            float x = (float) ((Math.random() * (map.getWidth()-1))) * Tile.SIZE;
            float y = (float) ((Math.random() * (map.getHeight()-1))) * Tile.SIZE;
            float x_diff = Math.abs(x - player.pos.x);
            float y_diff = Math.abs(y - player.pos.y);
            float mag = (float) Math.sqrt(x_diff*x_diff + y_diff*y_diff);
            if(mag > 140) {
                wood = new Wood(x, y, map, this);
                colliding = false;
            }
        }

        woodList.add(wood);
        allEntities.add(wood);
    }

    public void addAmmo(){

        AmmoPack ammo = null;

        boolean colliding = true;

        // Make sure enderman does not spawn on the player
        while(colliding){
            float x = (float) ((Math.random() * (map.getWidth()-1))) * Tile.SIZE;
            float y = (float) ((Math.random() * (map.getHeight()-1))) * Tile.SIZE;
            float x_diff = Math.abs(x - player.pos.x);
            float y_diff = Math.abs(y - player.pos.y);
            float mag = (float) Math.sqrt(x_diff*x_diff + y_diff*y_diff);
            if(mag > 140) {
                ammo = new AmmoPack(x, y, map, this);
                colliding = false;
            }
        }

        ammoList.add(ammo);
        allEntities.add(ammo);
    }

    public void addHealthKits(){

        HealthKit healthKit = null;

        boolean colliding = true;

        // Make sure enderman does not spawn on the player
        while(colliding){
            float x = (float) ((Math.random() * (map.getWidth()-1))) * Tile.SIZE;
            float y = (float) ((Math.random() * (map.getHeight()-1))) * Tile.SIZE;
            float x_diff = Math.abs(x - player.pos.x);
            float y_diff = Math.abs(y - player.pos.y);
            float mag = (float) Math.sqrt(x_diff*x_diff + y_diff*y_diff);
            if(mag > 140) {
                healthKit = new HealthKit(x, y, map, this);
                colliding = false;
            }
        }

        healthKitList.add(healthKit);
        allEntities.add(healthKit);
    }

    public void addFootprint(Footprint footprint) {

        this.footprints.add(footprint);
        this.allEntities.add(footprint);
    }


    public void addShell(Shell shell) {

        this.shells.add(shell);
        this.allEntities.add(shell);
    }


    public void addBlood(Blood blood) {

        this.blood.add(blood);
        this.allEntities.add(blood);
    }


    public static Vector2 getMousePos() {

        return mousePos;
    }


    public static Vector2 getTranslatedMousePos() {

        Vector2 translated = new Vector2(camera.position.x, camera.position.y);
        return translated.sub(mousePos.x, mousePos.y);
    }

    @Override
    public void render(float dt) {

        tick(dt);
        draw(main.batch, main.shapeRenderer);

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }

    public void pause() {}

    public void resume() {}

    public void show() {}

    public void hide() {}


    // INPUT PROCESSING
    //

    @Override
    public boolean keyDown(int keycode) {

        // Player controls
        //
        if(keycode == Input.Keys.W) {
            player.setUp(true);
        }
        if(keycode == Input.Keys.A) {
            player.setLeft(true);
        }
        if(keycode == Input.Keys.S) {
            player.setDown(true);
        }
        if(keycode == Input.Keys.D) {
            player.setRight(true);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        // Player controls
        //
        if(keycode == Input.Keys.W) {
            player.setUp(false);
        }
        if(keycode == Input.Keys.A) {
            player.setLeft(false);
        }
        if(keycode == Input.Keys.S) {
            player.setDown(false);
        }
        if(keycode == Input.Keys.D) {
            player.setRight(false);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) { return false; }

    // MOUSE INPUT
    //

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(button == 0) {
            if(player.isWeaponReady()) {
                rumble.rumble(5, 6);
                for (Entity mob : bluemen)
                    if (player.withinRange(mob))
                        ((Blueman) mob).die();

//                for (Entity mob : endermans)
//                    if (player.withinLight(mob))
//                        ((Enderman) mob).reverseDirection();

            }
            player.attack();
        }

        if(button == 1) {
            player.raiseWeapon();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(button == 1) {
            player.lowerWeapon();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        this.mousePos.set(x, y);
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {

        this.mousePos.set(x, y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        camera.zoom += ((float) (amount) / 5);
        return false;
    }

}
