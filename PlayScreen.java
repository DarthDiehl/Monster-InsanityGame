package com.monsterinsanity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayScreen extends ScreenAdapter {
	private OrthographicCamera camera;
	private FitViewport viewport;
	private ShapeRenderer shapes;

	// world size (logical units)
	private static final float WORLD_WIDTH = 16f;
	private static final float WORLD_HEIGHT = 9f;

	// player position
	private float px = WORLD_WIDTH / 2f;
	private float py = WORLD_HEIGHT / 2f;

	@Override
	public void show() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
		camera.update();

		shapes = new ShapeRenderer();
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shapes.setProjectionMatrix(camera.combined);
		shapes.begin(ShapeRenderer.ShapeType.Filled);
		shapes.setColor(Color.WHITE);
		shapes.circle(px, py, 0.3f);
		shapes.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		shapes.dispose();
	}
}
