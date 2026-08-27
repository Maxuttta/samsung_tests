package com.mygdx.game;
import com.badlogic.gdx.utils.TimeUtils;
public class GameSession { public long sessionStartTime,nextTrashSpawnTime,nextEnemySpawnTime; public GameState state=GameState.PLAYING; public int score;
 public void startGame(){sessionStartTime=TimeUtils.millis();nextTrashSpawnTime=sessionStartTime+GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN;nextEnemySpawnTime=sessionStartTime+30000;state=GameState.PLAYING;score=0;}
 public boolean shouldSpawnTrash(){if(state!=GameState.PLAYING)return false;if(nextTrashSpawnTime<=TimeUtils.millis()){nextTrashSpawnTime=TimeUtils.millis()+(long)getTrashPeriodCoolDown();return true;}return false;}
 public boolean shouldSpawnEnemy(){if(state!=GameState.PLAYING)return false;if(nextEnemySpawnTime<=TimeUtils.millis()){nextEnemySpawnTime+=30000;return true;}return false;}
 private float getTrashPeriodCoolDown(){return Math.max(450f,2000f*(float)Math.exp(-.001f*(TimeUtils.millis()-sessionStartTime)/1000f));}
}
