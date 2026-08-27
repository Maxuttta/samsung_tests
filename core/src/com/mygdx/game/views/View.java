package com.mygdx.game.views;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public abstract class View { protected float x,y,width,height; public View(float x,float y){this.x=x;this.y=y;} public View(float x,float y,float width,float height){this(x,y);this.width=width;this.height=height;} public boolean isHit(float tx,float ty){return tx>=x&&tx<=x+width&&ty>=y&&ty<=y+height;} public abstract void draw(SpriteBatch batch); public abstract void dispose(); }
