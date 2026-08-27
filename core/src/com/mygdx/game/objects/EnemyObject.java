package com.mygdx.game.objects;
import com.badlogic.gdx.physics.box2d.World; import com.mygdx.game.GameResources;
public class EnemyObject extends TrashObject { public EnemyObject(int x,int y,World world){super(120,120,GameResources.SHIP_IMG_PATH,world,x,y);} @Override public void move(){body.setLinearVelocity(0,-70);}}
