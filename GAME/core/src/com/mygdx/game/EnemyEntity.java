package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/** Enemy movement and collision state. */
public final class EnemyEntity {
    private final Rectangle bounds;
    private final Rectangle hitBounds = new Rectangle();
    private final float patrolStart;
    private final float patrolEnd;
    private float direction = 1f;
    private float velocityY;
    private boolean grounded;
    private boolean defeated;

    public EnemyEntity(float x, float y, float patrolStart, float patrolEnd) {
        bounds = new Rectangle(x, y, 34f, 30f);
        this.patrolStart = patrolStart;
        this.patrolEnd = patrolEnd;
        updateHitBounds();
    }

    public Rectangle bounds() { return bounds; }
    public Rectangle hitBounds() { return hitBounds; }
    public float patrolStart() { return patrolStart; }
    public float patrolEnd() { return patrolEnd; }
    public float direction() { return direction; }
    public void reverse() { direction = -direction; }
    public float velocityY() { return velocityY; }
    public void velocityY(float value) { velocityY = value; }
    public boolean grounded() { return grounded; }
    public void grounded(boolean value) { grounded = value; }
    public boolean isDefeated() { return defeated; }
    public void defeat() { defeated = true; }
    public void updateHitBounds() { hitBounds.set(bounds.x + 5f, bounds.y + 2f, 24f, 25f); }
}
