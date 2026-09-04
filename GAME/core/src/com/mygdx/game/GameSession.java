package com.mygdx.game;

/** Mutable state for one play-through. Rendering and input do not belong here. */
public final class GameSession {
    private GameMode mode = GameMode.MENU;
    private int level;
    private int score;
    private int lives = 3;
    private float elapsed;
    private String endMessage = "";

    public GameMode mode() { return mode; }
    public void mode(GameMode mode) { this.mode = mode; }
    public int level() { return level; }
    public void level(int level) { this.level = level; }
    public int score() { return score; }
    public void score(int score) { this.score = score; }
    public void addScore(int points) { score += points; }
    public int lives() { return lives; }
    public void lives(int lives) { this.lives = lives; }
    public int loseLife() { return --lives; }
    public float elapsed() { return elapsed; }
    public void advance(float delta) { elapsed += delta; }
    public void resetTimer() { elapsed = 0f; }
    public String endMessage() { return endMessage; }
    public void endMessage(String message) { endMessage = message; }
    public void beginLevel(int level) {
        this.level = level;
        score = 0;
        lives = 3;
        elapsed = 0f;
        endMessage = "";
        mode = GameMode.PLAYING;
    }
}
