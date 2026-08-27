package com.mygdx.game.objects;
import com.badlogic.gdx.math.Vector2; import com.badlogic.gdx.math.Vector3; import com.badlogic.gdx.physics.box2d.World; import com.badlogic.gdx.utils.TimeUtils; import com.badlogic.gdx.graphics.g2d.SpriteBatch; import com.mygdx.game.GameSettings;
public class ShipObject extends GameObject { private int livesLeft=3; private long lastShotTime=0;
 public ShipObject(int x,int y,int width,int height,String texturePath,World world){super(texturePath,x,y,width,height,GameSettings.SHIP_BIT,world);}
 public void move(Vector3 v){body.setLinearVelocity(0,0);body.setTransform(v.x*GameSettings.SCALE,v.y*GameSettings.SCALE,body.getAngle());}
 public void putInFrame(){if(getY()>GameSettings.SCREEN_HEIGHT/2f-height/2f)setY((int)(GameSettings.SCREEN_HEIGHT/2f-height/2f));if(getY()<height/2f)setY(height/2);if(getX()<-width/2f)setX(GameSettings.SCREEN_WIDTH+width/2);if(getX()>GameSettings.SCREEN_WIDTH+width/2f)setX(-width/2);}
 @Override public void draw(SpriteBatch batch){putInFrame();super.draw(batch);} public void hit(){if(livesLeft>0)livesLeft--;} public boolean isAlive(){return livesLeft>0;} public int getLives(){return livesLeft;}
 public boolean needToShoot(){return TimeUtils.millis()-lastShotTime>=GameSettings.SHOOTING_COOL_DOWN;} public void markShot(){lastShotTime=TimeUtils.millis();}
}
