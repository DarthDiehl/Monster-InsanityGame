package com.monsterinsanity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UiHud {
	private final Stage stage = new Stage(new ScreenViewport());
    private final ShapeRenderer shapes = new ShapeRenderer();
    private int hp = 100;

    public Stage getStage() { return stage; }

    public void draw(float dt) {
        stage.act(dt);
        stage.draw();

        // simple HP bar
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        float barW = 200, barH = 14, x = 20, y = Gdx.graphics.getHeight() - 30;
        shapes.rect(x, y, barW, barH);
        shapes.end();
    }

    // temporary: draw player as a dot in world-coordinates (after map render)
    public void drawPlayerDot(com.badlogic.gdx.graphics.Camera camera, float wx, float wy) {
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.circle(wx, wy, 0.15f); // ~5px at 32px tiles
        shapes.end();
    }

    public void dispose() {
        stage.dispose();
        shapes.dispose();
    }
}
