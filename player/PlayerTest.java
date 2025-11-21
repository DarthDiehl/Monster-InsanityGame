package com.monsterinsanity.player;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    // 1 — Constructor sets X correctly-----------------------------------------------
    @Test
    void constructorSetsX_Positive() {
        // Arrange & Act
        Player player = new Player(5f, 10f);

        // Assert
        assertEquals(5f, player.getX());
    }

    // 2 — Constructor sets Y correctly----------------------------------------------
    @Test
    void constructorSetsY_Positive() {
        // Arrange & Act
        Player player = new Player(5f, 10f);

        // Assert
        assertEquals(10f, player.getY());
    }

    // 3 — setPosition updates X------------------------------------------------------
    @Test
    void setPosition_UpdatesX_Positive() {
        // Arrange
        Player player = new Player(0f, 0f);

        // Act
        player.setPosition(20f, 0f);

        // Assert
        assertEquals(20f, player.getX());
    }

    // 4 — setPosition updates Y-------------------------------------------------------
    @Test
    void setPosition_UpdatesY_Positive() {
        // Arrange
        Player player = new Player(0f, 0f);

        // Act
        player.setPosition(0f, 30f);

        // Assert
        assertEquals(30f, player.getY());
    }

    // 5 — setPosition sets both X and Y--------------------------------------------------
    @Test
    void setPosition_SetsBoth_Positive() {
        // Arrange
        Player player = new Player(0f, 0f);

        // Act
        player.setPosition(7f, 9f);

        // Assert
        assertEquals(7f, player.getX());
        assertEquals(9f, player.getY());
    }

    // 6 — Negative test: set negative coordinates----------------------------------------
    @Test
    void setPosition_NegativeCoordinates_Negative() {
        // Arrange
        Player player = new Player(0f, 0f);

        // Act
        player.setPosition(-5f, -8f);

        // Assert
        assertEquals(-5f, player.getX());
        assertEquals(-8f, player.getY());
    }

    // 7 — Boundary test: very large numbers---------------------------------------------
    @Test
    void setPosition_LargeValues_Boundary() {
        // Arrange
        Player player = new Player(0f, 0f);

        // Act
        player.setPosition(100000f, 200000f);

        // Assert
        assertEquals(100000f, player.getX());
        assertEquals(200000f, player.getY());
    }

    // 8 — Boundary test: zero values----------------------------------------------------
    @Test
    void setPosition_ZeroValues_Boundary() {
        // Arrange
        Player player = new Player(999f, 999f);

        // Act
        player.setPosition(0f, 0f);

        // Assert
        assertEquals(0f, player.getX());
        assertEquals(0f, player.getY());
    }

    // 9 — Player stores position independently of constructor values--------------------
    @Test
    void setPosition_OverridesConstructor_Positive() {
        // Arrange
        Player player = new Player(1f, 1f);

        // Act
        player.setPosition(50f, 60f);

        // Assert
        assertEquals(50f, player.getX());
        assertEquals(60f, player.getY());
    }
}
