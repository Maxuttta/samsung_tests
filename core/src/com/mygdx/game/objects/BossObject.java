package com.mygdx.game.objects;
import com.badlogic.gdx.physics.box2d.World; import com.badlogic.gdx.graphics.g2d.SpriteBatch; import com.badlogic.gdx.utils.TimeUtils; import com.mygdx.game.GameResources;
public class BossObject extends TrashObject { private int health=3; private long lastShot; private final int targetY;
 public BossObject(int x,int y,World world){super(180,180,GameResources.SHIP_IMG_PATH,world,x,y);targetY=930;}
 @Override public void move(){if(getY()>targetY)body.setLinearVelocity(0,-3);else body.setLinearVelocity(0,0);}
 public boolean isReadyToShoot(){return getY()<=targetY+10&&TimeUtils.millis()-lastShot>=1200;}
 public void markShot(){lastShot=TimeUtils.millis();}
 @Override public void hit(){if(health>0)health--;}
 public boolean isDestroyed(){return health<=0;}
 @Override public boolean hasToBeDestroyed(){return isDestroyed();}
 @Override public void draw(SpriteBatch batch){super.draw(batch);}
}
