package com.monsterinsanity;

import com.badlogic.gdx.Game;

public class MonsterInsanity extends Game {

    @Override
    public void create() {
        // Start on the main menu instead of directly in the game
        setScreen(new MainMenuScreen(this));
    }
}
