package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/** A collectible object placed in a level. */
public final class Collectible {
    private final Rectangle bounds;
    private boolean collected;
    private float breakTime = -1f;

    public Collectible(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle bounds() { return bounds; }
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }
    public float breakTime() { return breakTime; }
    public void startBreaking() { breakTime = 0f; }
    public void advanceBreaking(float delta) {
        if (breakTime >= 0f) breakTime += delta;
        if (breakTime >= 0.48f) collected = true;
    }
}
