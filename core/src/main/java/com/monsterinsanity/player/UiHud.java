package com.monsterinsanity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UiHud {
    private final Stage stage = new Stage(new ScreenViewport()); // keep for future UI
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont(); // built-in default font

    private float hpPct = 1f;
    private float stamPct = 1f;
    private String helpText = "";

    public Stage getStage() { return stage; }
    
    public void setHelpText(String text) { this.helpText = (text == null) ? "" : text; }

    public void setBars(float hp, float stam) { 
        this.hpPct = clamp01(hp); 
        this.stamPct = clamp01(stam); 
    }
    
    public void draw(float dt) {
        stage.act(dt);
        stage.draw();

        // Bars (screen space)
        shapes.setProjectionMatrix(stage.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        float y = Gdx.graphics.getHeight() - 18;

        // HP bar (green)
        shapes.setColor(0f, 0.8f, 0f, 1f);
        shapes.rect(16, y, 200 * hpPct, 8);

        // Stamina bar (yellow), just below HP
        shapes.setColor(0.95f, 0.85f, 0.2f, 1f);
        shapes.rect(16, y - 12, 200 * stamPct, 6);
        
        shapes.end();

        // Help text (bottom-left)
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        font.draw(batch, helpText, 16, 24);
        batch.end();
    }

    public void dispose() {
        stage.dispose();
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }

    private static float clamp01(float v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }
}
