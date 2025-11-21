package com.monsterinsanity.maps;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** Blocks movement against objects in Object Layer named "collision". */
public class CollisionSystem {
    private final TiledMap map;
    private final float unitScale;          // MUST match your map renderer scale (e.g. 1f/PPM)
    private final Array<Rectangle> solids = new Array<>();
    private final Array<Rectangle> caveZones = new Array<>(); // optional triggers

    public CollisionSystem(TiledMap map, float unitScale) {
        this.map = map;
        this.unitScale = unitScale;
        loadObjects();
    }

    public void reload() { loadObjects(); }

    private void loadObjects() {
        solids.clear();
        caveZones.clear();

        // collision layer
        MapLayer layer = map.getLayers().get("collision");
        if (layer != null) {
            for (MapObject o : layer.getObjects()) {
                if (o instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) o).getRectangle();
                    solids.add(toWorld(r));
                }
            }
        }

        // caves layer (not working, would it be spawn or map?)
        MapLayer cavesLayer = map.getLayers().get("caves");
        if (cavesLayer != null) {
            for (MapObject o : cavesLayer.getObjects()) {
                if (o instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) o).getRectangle();
                    caveZones.add(toWorld(r));
                }
            }
        }
    }

    private Rectangle toWorld(Rectangle px) {
        return new Rectangle(px.x * unitScale, px.y * unitScale,
                             px.width * unitScale, px.height * unitScale);
    }

    public boolean isBlocked(float wx, float wy) {
        for (Rectangle r : solids) if (r.contains(wx, wy)) return true;
        return false;
    }

    /** Optional: true if point is inside any "caves" rectangle trigger. */
    public boolean isOnCave(float wx, float wy) {
        for (Rectangle r : caveZones) if (r.contains(wx, wy)) return true;
        return false;
    }

    /** reliable movement for a point-sized player with sub-steps */
    public Vector2 movePoint(float fromX, float fromY, float toX, float toY) {
        float x = fromX, y = fromY;
        float dx = toX - fromX, dy = toY - fromY;

        float step = 0.5f; // world units; half a tile if your tiles are 1x1 world units
        int steps = Math.max(1, (int)Math.ceil(len(dx, dy) / step));
        float sx = dx / steps, sy = dy / steps;

        for (int i = 0; i < steps; i++) {
            float nx = x + sx;
            if (!isBlocked(nx, y)) x = nx;
            float ny = y + sy;
            if (!isBlocked(x, ny)) y = ny;
        }
        return new Vector2(x, y);
    }

    private static float len(float x, float y) {
        return (float)Math.sqrt(x * x + y * y);
    }
}