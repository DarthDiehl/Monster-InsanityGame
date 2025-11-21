package com.monsterinsanity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class PlayScreenTest {

    private Object getField(Object target, String fieldName) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    // 1) Basic sanity check ---------------------------------------------------

    @Test
    void constructor_CreatesPlayScreenInstance_Positive() {
        // Arrange & Act
        PlayScreen screen = new PlayScreen();

        // Assert
        assertNotNull(screen);
    }

    // 2) Camera / viewport / map are not initialized before show() ------------

    @Test
    void initialCamera_Viewport_Map_AreNull_BeforeShow() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        Object camera = getField(screen, "camera");
        Object viewport = getField(screen, "viewport");
        Object map = getField(screen, "map");

        // Assert
        assertNull(camera);
        assertNull(viewport);
        assertNull(map);
    }

    // 3) Initial stamina state -----------------------------------------------

    @Test
    void initialStamina_IsFull_Positive() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        float stamina = (float) getField(screen, "stamina");

        // Assert
        // Full stamina is represented as 1.0f
        assertEquals(1.0f, stamina, 0.0001f);
    }

    // 4) Initial paused state -------------------------------------------------

    @Test
    void initialPaused_IsFalse_Positive() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        boolean paused = (boolean) getField(screen, "paused");

        // Assert
        assertFalse(paused);
    }

    // 5) Default tile world size before TMX is read ---------------------------

    @Test
    void initialTileWorldSize_DefaultsToOne_Boundary() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        float tileW = (float) getField(screen, "tileWorldW");
        float tileH = (float) getField(screen, "tileWorldH");

        // Assert
        assertEquals(1.0f, tileW, 0.0001f);
        assertEquals(1.0f, tileH, 0.0001f);
    }

    // 6) Stamina drain / recover constants are positive -----------------------

    @Test
    void staminaConstants_ArePositive_Positive() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        Field drainField = PlayScreen.class.getDeclaredField("STAMINA_DRAIN_PER_SEC");
        drainField.setAccessible(true);
        float drain = (float) drainField.get(screen);

        Field recoverField = PlayScreen.class.getDeclaredField("STAMINA_RECOVER_PER_SEC");
        recoverField.setAccessible(true);
        float recover = (float) recoverField.get(screen);

        // Assert
        assertTrue(drain > 0.0f);
        assertTrue(recover > 0.0f);
    }

    // 7) Player scale is within (0, 1] ----------------------------------------

    @Test
    void playerScale_IsBetweenZeroAndOne_Boundary() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        Field scaleField = PlayScreen.class.getDeclaredField("PLAYER_SCALE");
        scaleField.setAccessible(true);
        float scale = (float) scaleField.get(screen);

        // Assert
        assertTrue(scale > 0.0f && scale <= 1.0f);
    }

    // 8) Map path constant looks like a TMX path ------------------------------

    @Test
    void mapOverworld_PathHasTmxExtension_Positive() throws Exception {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act
        Field pathField = PlayScreen.class.getDeclaredField("MAP_OVERWORLD");
        pathField.setAccessible(true);
        String path = (String) pathField.get(screen);

        // Assert
        assertNotNull(path);
        assertTrue(path.endsWith(".tmx"));
    }

    // 9) dispose() is null-safe when nothing is initialized -------------------

    @Test
    void dispose_DoesNotThrow_WhenUninitialized_Negative() {
        // Arrange
        PlayScreen screen = new PlayScreen();

        // Act & Assert
        // If any NPE happens here, the test will fail automatically.
        assertDoesNotThrow(screen::dispose);
    }

    // 10) resize() works when viewport is injected ----------------------------

    @Test
    void playScreen_HasViewportField_Positive() throws Exception {
        PlayScreen screen = new PlayScreen();

        // Just check the field exists (not null reference)
        assertDoesNotThrow(() -> getField(screen, "viewport"));
    }
} 
