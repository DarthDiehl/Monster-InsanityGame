package com.monsterinsanity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.monsterinsanity.maps.CollisionSystem;
import com.monsterinsanity.maps.MapLoader;
import com.monsterinsanity.player.Player;
import com.monsterinsanity.player.PlayerInput;
import com.monsterinsanity.player.UiHud;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class PlayScreen extends ScreenAdapter {
	private OrthographicCamera camera;
    private FitViewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private PlayerInput input;
    private CollisionSystem collision;
    private UiHud hud;

    // world scale: 32 pixels per tile
    public static final float PPM = 32f;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(16, 9, camera); // logical units, not pixels
        camera.position.set(8, 4.5f, 0);

        map = MapLoader.load("maps/overworld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f / PPM);

        collision = new CollisionSystem(map, PPM);

        player = new Player(5f, 5f); // tile (x,y)
        input = new PlayerInput();
        hud = new UiHud();

        Gdx.input.setInputProcessor(hud.getStage()); // HUD handles UI; movement reads from Gdx.input directly
    }

    @Override
    public void render(float dt) {
        // update
        float moveX = input.getMoveX();
        float moveY = input.getMoveY();
        player.update(dt, moveX, moveY, collision);

        // camera follows player
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // draw
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        // draw player (temp: simple rectangle)
        hud.drawPlayerDot(camera, player.getX(), player.getY());

        hud.draw(dt);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        hud.dispose();
    }
}
