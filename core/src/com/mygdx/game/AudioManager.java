package com.mygdx.game;
import com.badlogic.gdx.Gdx; import com.badlogic.gdx.audio.*;
public class AudioManager { public boolean isMusicOn,isSoundOn; private final Music music; private final Sound shoot,destroy;
 public AudioManager(){isMusicOn=MemoryManager.loadIsMusicOn();isSoundOn=MemoryManager.loadIsSoundOn();music=Gdx.audio.newMusic(Gdx.files.internal(GameResources.MUSIC_PATH));shoot=Gdx.audio.newSound(Gdx.files.internal(GameResources.SHOOT_SOUND_PATH));destroy=Gdx.audio.newSound(Gdx.files.internal(GameResources.DESTROY_SOUND_PATH));music.setLooping(true);updateMusicFlag();}
 public void updateMusicFlag(){if(isMusicOn){music.setVolume(.35f);music.play();}else music.pause();} public void updateSoundFlag(){} public void playShoot(){if(isSoundOn)shoot.play(.4f);} public void playDestroy(){if(isSoundOn)destroy.play(.5f);} public void dispose(){music.dispose();shoot.dispose();destroy.dispose();}
}
