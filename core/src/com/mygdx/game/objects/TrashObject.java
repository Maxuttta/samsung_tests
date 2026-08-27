package com.mygdx.game.objects;
import com.badlogic.gdx.physics.box2d.World; import com.mygdx.game.GameSettings; import java.util.Random;
public class TrashObject extends GameObject { private static final int paddingHorizontal=30; private int livesLeft=1; private boolean hit;
 public TrashObject(int width,int height,String texturePath,World world){this(width,height,texturePath,world,new Random().nextInt(Math.max(1,GameSettings.SCREEN_WIDTH-2*paddingHorizontal-width))+paddingHorizontal+width/2,GameSettings.SCREEN_HEIGHT+height/2);}
 public TrashObject(int width,int height,String texturePath,World world,int x,int y){super(texturePath,x,y,width,height,GameSettings.TRASH_BIT,world);}
 public boolean isInFrame(){return getY()+height/2>0;} public void move(){body.setLinearVelocity(0,-6); } public void hit(){hit=true;livesLeft--;} public boolean hasToBeDestroyed(){return hit||!isInFrame();}
}
