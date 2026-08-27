package com.mygdx.game.objects;
import com.badlogic.gdx.physics.box2d.World; import com.mygdx.game.GameResources;
public class BossBulletObject extends TrashObject { public BossBulletObject(int x,int y,World world){super(25,55,GameResources.BULLET_IMG_PATH,world,x,y);} @Override public void move(){body.setLinearVelocity(0,-8);}}
