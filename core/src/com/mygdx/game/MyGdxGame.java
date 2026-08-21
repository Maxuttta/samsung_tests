package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    private static final float GRAVITY = -900f, FLAP_SPEED = 390f, TUBE_SPEED = 210f;
    private static final float TUBE_WIDTH = 76f, GAP_HEIGHT = 200f;
    private static final float PAIR_GAP = TUBE_WIDTH * 5f;
    private static final float PAIR_STEP = TUBE_WIDTH + PAIR_GAP;
    private static final int TUBE_PAIRS = 8;
    private enum Screen { MENU, GAME, RESTART }

    private SpriteBatch batch;
    private ShapeRenderer shapes;
    private BitmapFont font;
    private GlyphLayout textLayout;
    private Texture menuBackground, gameBackground, restartBackground, buttonTexture;
    private Texture tubeTexture, tubeFlippedTexture;
    private TextureRegion[] birdFrames;
    private Screen screen = Screen.MENU;
    private float birdX, birdY, birdVelocity, animationTime, backgroundOffset;
    private float[] tubeX = new float[TUBE_PAIRS], gapBottom = new float[TUBE_PAIRS];
    private int score;
    private boolean[] passedTube = new boolean[TUBE_PAIRS];

    @Override public void create() {
        batch = new SpriteBatch(); shapes = new ShapeRenderer(); font = new BitmapFont(); textLayout = new GlyphLayout();
        menuBackground = new Texture("pictures_for_game/background/restart_bg.png");
        gameBackground = new Texture("pictures_for_game/background/game_bg.png");
        restartBackground = new Texture("pictures_for_game/background/restart_bg.png");
        buttonTexture = new Texture("pictures_for_game/button/button_bg.png");
        tubeTexture = new Texture("pictures_for_game/tube/tube.png");
        tubeFlippedTexture = new Texture("pictures_for_game/tube/tube_flipped.png");
        birdFrames = new TextureRegion[] {
            new TextureRegion(new Texture("pictures_for_game/bird/bird0.png")),
            new TextureRegion(new Texture("pictures_for_game/bird/bird1.png")),
            new TextureRegion(new Texture("pictures_for_game/bird/bird2.png"))
        };
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override public boolean touchDown(int x, int y, int pointer, int button) {
                return handleTap(x, Gdx.graphics.getHeight() - y);
            }
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) return handleTap(0, 0);
                if (keycode == Input.Keys.ESCAPE && screen != Screen.GAME) {
                    if (screen == Screen.RESTART) screen = Screen.MENU; else Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean handleTap(float x, float y) {
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        if (screen == Screen.GAME) { birdVelocity = FLAP_SPEED; return true; }
        if (screen == Screen.MENU) {
            if (buttonHit(x, y, w * .16f, h * .40f, w * .68f, h * .12f)) startGame();
            else if (buttonHit(x, y, w * .16f, h * .25f, w * .68f, h * .12f)) Gdx.app.exit();
        } else {
            if (buttonHit(x, y, w * .16f, h * .40f, w * .68f, h * .12f)) startGame();
            else if (buttonHit(x, y, w * .16f, h * .25f, w * .68f, h * .12f)) screen = Screen.MENU;
        }
        return true;
    }

    private boolean buttonHit(float x, float y, float bx, float by, float bw, float bh) {
        return x >= bx && x <= bx + bw && y >= by && y <= by + bh;
    }

    private void startGame() {
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        birdX = w * .24f; birdY = h * .55f; birdVelocity = 0;
        score = 0; animationTime = 0; backgroundOffset = 0;
        for (int i = 0; i < TUBE_PAIRS; i++) {
            tubeX[i] = w * .62f + i * PAIR_STEP;
            gapBottom[i] = h * (.34f + (i % 3) * .07f);
            passedTube[i] = false;
        }
        screen = Screen.GAME;
    }

    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
        if (screen == Screen.GAME) updateGame(delta);
        ScreenUtils.clear(.08f, .12f, .19f, 1f);
        if (screen == Screen.GAME) drawGame(); else drawMenu(screen == Screen.RESTART);
    }

    private void updateGame(float delta) {
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        birdVelocity += GRAVITY * delta; birdY += birdVelocity * delta;
        backgroundOffset = (backgroundOffset + TUBE_SPEED * .35f * delta) % w;
        animationTime += delta;
        float birdW = 68, birdH = 52;
        boolean hitTube = false;
        for (int i = 0; i < TUBE_PAIRS; i++) {
            tubeX[i] -= TUBE_SPEED * delta;
            if (!passedTube[i] && tubeX[i] + TUBE_WIDTH < birdX) { score++; passedTube[i] = true; }
            if (tubeX[i] + TUBE_WIDTH < 0) {
                float lastTubeX = tubeX[0];
                for (int j = 1; j < TUBE_PAIRS; j++) lastTubeX = Math.max(lastTubeX, tubeX[j]);
                tubeX[i] = lastTubeX + PAIR_STEP;
                gapBottom[i] = h * (.30f + ((score + i) % 3) * .08f);
                passedTube[i] = false;
            }
            hitTube |= birdX + birdW * .78f > tubeX[i] && birdX + birdW * .22f < tubeX[i] + TUBE_WIDTH
                && (birdY + birdH * .18f < gapBottom[i]
                || birdY + birdH * .82f > gapBottom[i] + GAP_HEIGHT);
        }
        if (birdY < 0 || birdY + birdH > h || hitTube) screen = Screen.RESTART;
    }

    private void drawGame() {
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        batch.begin();
        drawTiledBackground(gameBackground, backgroundOffset, w, h);
        for (int i = 0; i < TUBE_PAIRS; i++) {
            batch.draw(tubeTexture, tubeX[i], 0, TUBE_WIDTH, gapBottom[i]);
            batch.draw(tubeFlippedTexture, tubeX[i], gapBottom[i] + GAP_HEIGHT,
                TUBE_WIDTH, h - gapBottom[i] - GAP_HEIGHT);
        }
        int frame = ((int) (animationTime * 9)) % birdFrames.length;
        batch.draw(birdFrames[frame], birdX, birdY, 68, 52);
        batch.end(); drawText(String.valueOf(score), w * .5f, h * .90f, 2.3f);
    }

    private void drawMenu(boolean restart) {
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        batch.begin(); drawTiledBackground(restart ? restartBackground : menuBackground, 0, w, h); batch.end();
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.setColor(new Color(.04f, .08f, .15f, .65f));
        shapes.rect(w * .10f, h * .18f, w * .80f, h * .64f); shapes.end();
        drawText(restart ? "GAME OVER" : "FLAPPY BIRD", w * .5f, h * .73f, 2.2f);
        if (restart) drawText("SCORE: " + score, w * .5f, h * .64f, 1.5f);
        drawButton("START GAME", w * .16f, h * .40f, w * .68f, h * .12f);
        drawButton(restart ? "MENU" : "EXIT", w * .16f, h * .25f, w * .68f, h * .12f);
    }

    private void drawButton(String label, float x, float y, float width, float height) {
        batch.begin(); batch.draw(buttonTexture, x, y, width, height); batch.end();
        drawText(label, x + width / 2, y + height * .37f, Math.max(1.1f, width / 280f));
    }

    private void drawText(String text, float x, float y, float scale) {
        font.getData().setScale(scale); font.setColor(Color.WHITE); textLayout.setText(font, text);
        batch.begin(); font.draw(batch, text, x - textLayout.width / 2, y); batch.end();
    }

    private void drawTiledBackground(Texture texture, float offset, float width, float height) {
        batch.draw(texture, -offset, 0, width, height);
        batch.draw(texture, width - offset, 0, width, height);
    }

    @Override public void dispose() {
        batch.dispose(); shapes.dispose(); font.dispose();
        menuBackground.dispose(); gameBackground.dispose(); restartBackground.dispose(); buttonTexture.dispose();
        tubeTexture.dispose(); tubeFlippedTexture.dispose();
        for (TextureRegion frame : birdFrames) frame.getTexture().dispose();
    }
}
