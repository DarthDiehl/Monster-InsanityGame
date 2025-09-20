package com.monsterinsanity.maps;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public class CollisionSystem {
	private final TiledMap map;
    private final float ppm;
    private final int tileW, tileH;

    public CollisionSystem(TiledMap map, float ppm) {
        this.map = map;
        this.ppm = ppm;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0); // first layer
        this.tileW = (int)layer.getTileWidth();
        this.tileH = (int)layer.getTileHeight();
    }

    public boolean isBlocked(float worldX, float worldY) {
        int tileX = (int)((worldX * ppm) / tileW);
        int tileY = (int)((worldY * ppm) / tileH);
        
        for (MapLayer l : map.getLayers()) {
        	
            if (!(l instanceof TiledMapTileLayer)) continue;
            TiledMapTileLayer layer = (TiledMapTileLayer) l;
            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);
            
            if (cell == null) continue;
            TiledMapTile tile = cell.getTile();
            
            if (tile == null) continue;
            MapProperties p = tile.getProperties();
            
            if (p.containsKey("blocked") && Boolean.parseBoolean(p.get("blocked").toString())) {
                return true;
            }
        }
        return false;
    }

    // simple attempt move with collision
    public Vector2 tryMove(float fromX, float fromY, float toX, float toY) {
        // axis-separated collision to prevent corner clipping
        float nx = toX;
        float ny = toY;

        if (isBlocked(nx, fromY)) nx = fromX;
        if (isBlocked(nx, ny))   ny = fromY;

        return new Vector2(nx, ny);
    }
}
