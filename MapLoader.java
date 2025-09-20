package com.monsterinsanity.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapLoader {
	public static TiledMap load(String path) {
		return new TmxMapLoader().load(path);
	}
}
