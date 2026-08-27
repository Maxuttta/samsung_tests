package com.mygdx.game.objects;
import com.badlogic.gdx.physics.box2d.World; import com.mygdx.game.GameSettings;
public class BulletObject extends GameObject { private boolean wasHit;
 public BulletObject(int x,int y,int width,int height,String texturePath,World world){super(texturePath,x,y,width,height,GameSettings.BULLET_BIT,world);body.setLinearVelocity(0,GameSettings.BULLET_VELOCITY*GameSettings.SCALE);body.setBullet(true);}
 public void hit(){wasHit=true;} public boolean wasHit(){return wasHit;} public boolean hasToBeDestroyed(){return wasHit||getY()>GameSettings.SCREEN_HEIGHT+height/2;}
}
