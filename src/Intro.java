package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Intro implements Screen {


    private Game main;
    public static OrthographicCamera camera;
    private static Viewport viewport;

    private float alpha = 1f;
    private boolean fadingIn = false;
    private boolean fadingOut = false;
    private Timer inTimer;
    private Timer outTimer;
    private Texture title;


    public Intro(Game main) {

        this.main = main;
        this.camera = new OrthographicCamera();
        this.camera.position.set(main.WIDTH / 2, main.HEIGHT / 2, 0);
        this.viewport = new FitViewport(main.WIDTH, main.HEIGHT, camera);

        title = TextureStore.get().getTexture("title");
        inTimer = new Timer(1400, true);
        outTimer = new Timer(3800, false);
    }


    public void tick(float dt) {

        // Fade in
        if(inTimer.tick(dt)) {
            inTimer.stop();
            outTimer.start();
            fadingIn = true;
        }
        if(fadingIn) {
            alpha -= dt * 0.5f;
        }

        // Fade out
        if(outTimer.tick(dt)) {
            outTimer.stop();
            fadingIn = false;
            fadingOut = true;
        }
        if(fadingOut) {
            alpha += dt * 0.5f;
            if(alpha >= 1) {
                main.setScreen(new MyScreen(main));
            }
        }
    }


    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(title, (Game.WIDTH - title.getWidth()) / 2, (Game.HEIGHT - title.getHeight()) / 2);
        batch.end();

        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        main.fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        main.fadeRenderer.setColor(0, 0, 0, alpha);
        main.fadeRenderer.rect(0, 0, Game.WIDTH, Game.HEIGHT);
        main.fadeRenderer.end();
    }


    @Override
    public void render(float dt) {

        tick(dt);
        draw(main.batch, main.shapeRenderer);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }


    public void show() {}

    public void pause() {}

    public void resume() {}

    public void hide() {}

    public void dispose() {}
}
