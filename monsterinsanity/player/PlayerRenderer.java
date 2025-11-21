package com.monsterinsanity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerRenderer {
	private static final int FRAME_W = 16;
	private static final int FRAME_H = 16;
	private static final float FRAME_TIME = 0.12f;

	// row mapping (adjust if your sheet differs)
	private static final int ROW_DOWN  = 0;
	private static final int ROW_LEFT  = 2; // swap w/ up
	private static final int ROW_RIGHT = 3;
	private static final int ROW_UP    = 1; // swap w/ left
	private static final int WALK_FRAMES = 3;

	private final Texture sheet;
	private final TextureRegion[][] grid;
	private final Animation<TextureRegion> walkDown, walkLeft, walkRight, walkUp;
	private final TextureRegion idleDown, idleLeft, idleRight, idleUp;
	private float stateTime = 0f;
	private boolean moving = false;

	public enum Dir { DOWN, LEFT, RIGHT, UP }
	private Dir facing = Dir.DOWN;

	public PlayerRenderer() {
		sheet = new Texture(Gdx.files.internal("gfx/character1.png")); // Can update character once dropping png file into lwjgl3/assets/gfx
		sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

		walkDown  = animFromRow(ROW_DOWN,  WALK_FRAMES);
		walkLeft  = animFromRow(ROW_LEFT,  WALK_FRAMES);
		walkRight = animFromRow(ROW_RIGHT, WALK_FRAMES);
		walkUp    = animFromRow(ROW_UP,    WALK_FRAMES);

		idleDown  = grid[ROW_DOWN][0];
		idleLeft  = grid[ROW_LEFT][0];
		idleRight = grid[ROW_RIGHT][0];
		idleUp    = grid[ROW_UP][0];
	}

	private Animation<TextureRegion> animFromRow(int row, int desiredFrames) {
		int cols = Math.min(desiredFrames, grid[row].length);
		TextureRegion[] frames = new TextureRegion[cols];
		for (int i = 0; i < cols; i++) frames[i] = grid[row][i];
		return new Animation<>(FRAME_TIME, frames);
	}

	public void update(float dt, float mx, float my) {
		stateTime += dt;
		moving = (mx != 0 || my != 0);
		if (Math.abs(mx) > Math.abs(my)) facing = (mx > 0) ? Dir.RIGHT : Dir.LEFT;
		else if (Math.abs(my) > 0)       facing = (my > 0) ? Dir.UP   : Dir.DOWN;
	}

	public void draw(SpriteBatch batch, float wx, float wy, float ww, float wh) {
		batch.setColor(1f,1f,1f,1f);
		TextureRegion frame;
		switch (facing) {
		case LEFT:  frame = moving ? walkLeft.getKeyFrame(stateTime, true)  : idleLeft;  break;
		case RIGHT: frame = moving ? walkRight.getKeyFrame(stateTime, true) : idleRight; break;
		case UP:    frame = moving ? walkUp.getKeyFrame(stateTime, true)    : idleUp;    break;
		default:    frame = moving ? walkDown.getKeyFrame(stateTime, true)  : idleDown;  break;
		}
		batch.draw(frame, wx, wy, ww, wh);
	}

	public void dispose() { sheet.dispose(); }
}
