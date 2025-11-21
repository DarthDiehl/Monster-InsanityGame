package com.monsterinsanity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.monsterinsanity.maps.CollisionSystem;
import com.monsterinsanity.maps.MapLoader;
import com.monsterinsanity.player.Player;
import com.monsterinsanity.player.PlayerInput;
import com.monsterinsanity.player.PlayerRenderer;
import com.monsterinsanity.player.UiHud;
import com.badlogic.gdx.audio.Music;

public class PlayScreen extends ScreenAdapter {
    // render + camera
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch worldBatch;
    private OrthogonalTiledMapRenderer mapRenderer;

    // world / map
    private static final float PPM = 16f;             // pixels-per-meter (tiles usually 16 px)
    private static final float PLAYER_SCALE = 0.75f;  // draw sprite smaller than a tile
    private static final String MAP_OVERWORLD = "SampleMap/samplemap.tmx";

    private TiledMap map;
    private float tileWorldW = 1f; // tile width in world units (computed from TMX)
    private float tileWorldH = 1f; // tile height in world units (computed from TMX)

    // game play
    private Player player;
    private PlayerInput input;
    private PlayerRenderer playerRenderer;
    private CollisionSystem collision;
    private UiHud hud;

    // stamina
    private float stamina = 1f;
    private static final float STAMINA_DRAIN_PER_SEC   = 0.45f;
    private static final float STAMINA_RECOVER_PER_SEC = 0.25f;

    // pause
    private boolean paused = false;
    private BitmapFont pauseFont;

    // optional debug
    private static final boolean DEBUG_COLLISION = false;
    private ShapeRenderer debugShapes;
    
    // beats
    private Music bgm; 

    @Override
    public void show() {
        // camera: use a small tile-based world (16x9 tiles visible)
        camera = new OrthographicCamera();
        viewport = new FitViewport(16f, 9f, camera);
        camera.position.set(8f, 4.5f, 0f);

        // load overworld map
        map = MapLoader.load(MAP_OVERWORLD);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f / PPM);

        // compute tile size in world units (meters) from TMX properties
        MapProperties props = map.getProperties();
        Integer tw = props.get("tilewidth", Integer.class);
        Integer th = props.get("tileheight", Integer.class);
        if (tw == null) tw = 16;
        if (th == null) th = 16;
        tileWorldW = tw / PPM; // e.g., 16 px / 16 ppm = 1 world unit per tile
        tileWorldH = th / PPM;

        // systems / actors
        collision = new CollisionSystem(map, 1f / PPM);
        player = new Player(5f, 5f);
        input = new PlayerInput();
        hud = new UiHud();
        hud.setHelpText("Goodluck & Have Fun, ESC : Puase/Resume");

        worldBatch = new SpriteBatch();
        playerRenderer = new PlayerRenderer();

        if (DEBUG_COLLISION) debugShapes = new ShapeRenderer();

        pauseFont = new BitmapFont();
        pauseFont.getData().setScale(0.2f); 

        bgm = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm.mp3"));
        bgm.setLooping(true);
        bgm.play(); 
        
        // let the HUD receive UI input (if you add buttons later)
        Gdx.input.setInputProcessor(hud.getStage());
    }

    @Override
    public void render(float dt) {
        // pause with ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            
            if (paused) bgm.pause();
            else        bgm.play();
        }

        // read movement input
        float moveX = input.getMoveX();
        float moveY = input.getMoveY();
        boolean sprinting = input.isSprinting();

        if (!paused) {
            // stamina logic
            boolean moving = (moveX != 0f || moveY != 0f);
            if (sprinting && stamina <= 0f) sprinting = false;

            if (moving && sprinting) stamina -= STAMINA_DRAIN_PER_SEC * dt;
            else                      stamina += STAMINA_RECOVER_PER_SEC * dt;

            if (stamina < 0f) stamina = 0f;
            if (stamina > 1f) stamina = 1f;
            hud.setBars(1f, stamina);

            // update player (updates from CollisionSystem)
            player.update(dt, moveX, moveY, sprinting, collision);
        }

        // camera follow (even when paused)
        camera.position.set(player.getX(), player.getY(), 0f);
        camera.update();

        // clear & draw
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // draw player
        worldBatch.setProjectionMatrix(camera.combined);
        worldBatch.begin();
        playerRenderer.update(dt, moveX, moveY);

        float pw = tileWorldW * PLAYER_SCALE;
        float ph = tileWorldH * PLAYER_SCALE;
        float drawX = player.getX() + (tileWorldW - pw) * 0.5f;
        float drawY = player.getY() + (tileWorldH - ph) * 0.5f;

        playerRenderer.draw(worldBatch, drawX, drawY, pw, ph);

        // draw pause text overlay if paused
        if (paused) {
            pauseFont.draw(worldBatch,
                    "PAUSED",
                    player.getX() - 4f,
                    player.getY() + 4f);
        }

        worldBatch.end();
        
        // HUD last
        hud.draw(dt);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (map != null) map.dispose();
        if (worldBatch != null) worldBatch.dispose();
        if (playerRenderer != null) playerRenderer.dispose();
        if (hud != null) hud.dispose();
        if (debugShapes != null) debugShapes.dispose();
        if (pauseFont != null) pauseFont.dispose();
        if (bgm != null) bgm.dispose();
    }
}