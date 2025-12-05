package com.monsterinsanity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Simple main menu:
 *  - ENTER = start game
 *  - ESC   = quit
 */
public class MainMenuScreen extends ScreenAdapter {

    private final MonsterInsanity game;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(MonsterInsanity game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); // simple screen coords

        batch = new SpriteBatch();
        font = new BitmapFont(); // default LibGDX font
    }

    @Override
    public void render(float delta) {
        // --- input ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new PlayScreen());
            dispose();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // --- draw ---
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "MONSTER INSANITY", 260, 300);
        font.draw(batch, "Press ENTER to Play", 280, 250);
        font.draw(batch, "Press ESC to Quit", 295, 220);
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
    }
}