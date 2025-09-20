package com.monsterinsanity.player;

import com.badlogic.gdx.math.Vector2;
import com.monsterinsanity.maps.CollisionSystem;

public class Player {
	private final Vector2 pos = new Vector2();
	private final float speed = 3.5f; // tiles per second

	public Player(float startX, float startY) {
		pos.set(startX, startY);
	}

	public void update(float dt, float mx, float my, CollisionSystem collision) {
		// normalize diagonal
		if (mx != 0 || my != 0) {
			float len = (float)Math.sqrt(mx*mx + my*my);
			mx /= len; my /= len;
		}
		float toX = pos.x + mx * speed * dt;
		float toY = pos.y + my * speed * dt;

		Vector2 resolved = collision.tryMove(pos.x, pos.y, toX, toY);
		pos.set(resolved);
	}

	public float getX() { return pos.x; }
	public float getY() { return pos.y; }
}
