package com.mygdx.game.views;
import com.badlogic.gdx.graphics.Texture; import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class ImageView extends View { protected Texture texture; public ImageView(float x,float y,String path){super(x,y);texture=new Texture(path);width=texture.getWidth();height=texture.getHeight();} public void draw(SpriteBatch b){b.draw(texture,x,y,width,height);} public void dispose(){texture.dispose();} }
