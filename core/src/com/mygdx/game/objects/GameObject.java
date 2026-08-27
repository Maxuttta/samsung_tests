package com.mygdx.game.objects;
import com.badlogic.gdx.graphics.Texture; import com.badlogic.gdx.graphics.g2d.SpriteBatch; import com.badlogic.gdx.physics.box2d.*; import com.badlogic.gdx.math.Vector2; import com.mygdx.game.GameSettings;
public class GameObject {
 protected Texture texture; protected Body body; protected int width,height; protected World world; public short cBits;
 public GameObject(String texturePath,int x,int y,int width,int height,short cBits,World world){this.width=width;this.height=height;this.cBits=cBits;this.world=world;texture=new Texture(texturePath);body=createBody(x,y,world);}
 protected Body createBody(float x,float y,World world){BodyDef bd=new BodyDef();bd.type=BodyDef.BodyType.DynamicBody;bd.position.set(x*GameSettings.SCALE,y*GameSettings.SCALE);Body b=world.createBody(bd);b.setUserData(this);CircleShape shape=new CircleShape();shape.setRadius(Math.max(width,height)*GameSettings.SCALE/2f);FixtureDef fd=new FixtureDef();fd.shape=shape;fd.density=1;fd.isSensor=true;fd.filter.categoryBits=cBits;fd.filter.maskBits=(short)(GameSettings.TRASH_BIT|GameSettings.SHIP_BIT|GameSettings.BULLET_BIT);b.createFixture(fd);shape.dispose();return b;}
 public int getX(){return (int)(body.getPosition().x/GameSettings.SCALE);} public int getY(){return (int)(body.getPosition().y/GameSettings.SCALE);}
 public void setX(int x){body.setTransform(x*GameSettings.SCALE,body.getPosition().y,body.getAngle());} public void setY(int y){body.setTransform(body.getPosition().x,y*GameSettings.SCALE,body.getAngle());}
 public void draw(SpriteBatch batch){batch.draw(texture,getX()-width/2f,getY()-height/2f,width,height);} public void dispose(){texture.dispose();}
 public Body getBody(){return body;}
}
