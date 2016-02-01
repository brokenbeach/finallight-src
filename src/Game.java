package com.tedigc.ggj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Game extends com.badlogic.gdx.Game {


	public static final String TITLE = "Final Light";
	public static final int WIDTH = 1000;
	public static final int HEIGHT = (WIDTH * 9) / 16;

	public SpriteBatch batch;
	public SpriteBatch lightbatch;
	public ShapeRenderer shapeRenderer;
    public ShapeRenderer fadeRenderer;

    public  Music music, intro, click, collect, cut, explosion, fire, grunt, reload, shot, scream;

	@Override
	public void create () {

		batch = new SpriteBatch();
        lightbatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
        fadeRenderer = new ShapeRenderer();
        intro = Gdx.audio.newMusic(Gdx.files.internal("sfx/intro.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sfx/blizzard.wav"));
        click = Gdx.audio.newMusic(Gdx.files.internal("sfx/click.wav"));
        collect = Gdx.audio.newMusic(Gdx.files.internal("sfx/collect.wav"));
        cut = Gdx.audio.newMusic(Gdx.files.internal("sfx/cut.wav"));
        explosion = Gdx.audio.newMusic(Gdx.files.internal("sfx/explosion.wav"));
        fire = Gdx.audio.newMusic(Gdx.files.internal("sfx/fire.wav"));
        grunt = Gdx.audio.newMusic(Gdx.files.internal("sfx/grunt.wav"));
        reload = Gdx.audio.newMusic(Gdx.files.internal("sfx/reload.wav"));
        shot = Gdx.audio.newMusic(Gdx.files.internal("sfx/rifle.wav"));
        scream = Gdx.audio.newMusic(Gdx.files.internal("sfx/spooky.wav"));

        intro.setVolume(0.5f);
        music.setVolume(1f);
        music.play();
        music.setLooping(true);
		this.setScreen(new Intro(this));
	}

	@Override
	public void render () {

		super.render();
	}

}
