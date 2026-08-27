package com.mygdx.game.views;
import com.badlogic.gdx.graphics.g2d.*;
public class TextView extends View { protected BitmapFont font; protected String text; public TextView(BitmapFont f,float x,float y,String t){super(x,y);font=f;setText(t);} public void setText(String t){text=t;GlyphLayout l=new GlyphLayout(font,t);width=l.width;height=l.height;} public void draw(SpriteBatch b){font.draw(b,text,x,y+height);} public void dispose(){} }
