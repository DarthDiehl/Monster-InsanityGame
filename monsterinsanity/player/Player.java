package com.monsterinsanity.player;

import com.badlogic.gdx.math.Vector2;
import com.monsterinsanity.maps.CollisionSystem;

/** Player model with slower walk + sprint toggle. */
public class Player {
    private final Vector2 pos = new Vector2();
    private final float baseSpeed  = 4.0f;   // tiles per second (match your unitScale)
    private final float sprintMult = 2.0f;   // sprint speed multiplier

    public Player(float startX, float startY) {
        pos.set(startX, startY);
    }
    
    public void update(float dt, float mx, float my, boolean sprint, CollisionSystem collision) {
        // normalize diagonal so speed is consistent
        if (mx != 0 || my != 0) {
            float len = (float) Math.sqrt(mx * mx + my * my);
            mx /= len;
            my /= len;
        }

        float speed = baseSpeed * (sprint ? sprintMult : 1f);

        float toX = pos.x + mx * speed * dt;
        float toY = pos.y + my * speed * dt;

        // ⬇️ updated to use CollisionSystem.movePoint
        Vector2 resolved = collision.movePoint(pos.x, pos.y, toX, toY);
        pos.set(resolved);
    }

    public float getX() {
    	return pos.x; 
    }
    
    public float getY() {
    	return pos.y; 
    }
    
    public void setPosition(float x, float y) {
        pos.set(x, y);
    }
}
