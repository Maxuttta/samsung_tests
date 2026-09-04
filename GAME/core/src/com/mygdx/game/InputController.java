package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class InputController {
    public boolean pressed(int primary, int secondary) { return Gdx.input.isKeyPressed(primary) || Gdx.input.isKeyPressed(secondary); }
    public boolean justPressed(int key) { return Gdx.input.isKeyJustPressed(key); }
    public boolean touched(Rectangle area, Viewport viewport) {
        if (!Gdx.input.isTouched()) return false;
        Vector3 point = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(point);
        return area.contains(point.x, point.y);
    }
}
