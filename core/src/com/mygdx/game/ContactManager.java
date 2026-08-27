package com.mygdx.game;
import com.badlogic.gdx.physics.box2d.*; import com.mygdx.game.objects.*;
public class ContactManager implements ContactListener { private final AudioManager audio; public ContactManager(AudioManager audio){this.audio=audio;}
 private boolean pair(short a,short b,short x,short y){return(a==x&&b==y)||(a==y&&b==x);}
 @Override public void beginContact(Contact c){Object a=c.getFixtureA().getBody().getUserData(),b=c.getFixtureB().getBody().getUserData();if(!(a instanceof GameObject)||!(b instanceof GameObject))return;GameObject x=(GameObject)a,y=(GameObject)b;if(pair(x.cBits,y.cBits,GameSettings.TRASH_BIT,GameSettings.BULLET_BIT)){((TrashObject)(x.cBits==GameSettings.TRASH_BIT?x:y)).hit();((BulletObject)(x.cBits==GameSettings.BULLET_BIT?x:y)).hit();audio.playDestroy();}else if(pair(x.cBits,y.cBits,GameSettings.TRASH_BIT,GameSettings.SHIP_BIT)){((TrashObject)(x.cBits==GameSettings.TRASH_BIT?x:y)).hit();((ShipObject)(x.cBits==GameSettings.SHIP_BIT?x:y)).hit();audio.playDestroy();}}
 @Override public void endContact(Contact c){} @Override public void preSolve(Contact c,Manifold m){} @Override public void postSolve(Contact c,ContactImpulse i){}
}
