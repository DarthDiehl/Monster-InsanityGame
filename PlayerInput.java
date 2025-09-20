package com.monsterinsanity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerInput {
	
	public float getMoveX() {
        float x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 1;
        return x;
    }
	
    public float getMoveY() {
        float y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    y += 1;
        return y;
    }
}
