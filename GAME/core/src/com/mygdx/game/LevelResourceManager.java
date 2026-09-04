package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public final class LevelResourceManager {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public void load(String path) { unload(); map = new TmxMapLoader().load(path); renderer = new OrthogonalTiledMapRenderer(map); }
    public TiledMap map() { return map; }
    public OrthogonalTiledMapRenderer renderer() { return renderer; }
    public void unload() { if (renderer != null) { renderer.dispose(); renderer = null; } if (map != null) { map.dispose(); map = null; } }
}
