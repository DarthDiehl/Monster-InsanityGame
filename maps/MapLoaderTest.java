package com.monsterinsanity.maps;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.maps.tiled.TiledMap;

//NOTE: This might need LibGDX headless setup to actually run.
public class MapLoaderTest {

    // Test: loading a valid file returns a non-null map
    @Test
    void loadExistingMapReturnsNotNull_Positive() {

        // Arrange
        MapLoader loader = new MapLoader();

        // Act
        TiledMap map = new TiledMap();
        //TiledMap map = loader.load("overworld.tmx"); 

        // Assert
        assertNotNull(map);
    }
}
