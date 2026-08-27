package com.mygdx.game.views;
import com.badlogic.gdx.graphics.g2d.BitmapFont; import java.util.List; import com.mygdx.game.GameSettings;
public class RecordsListView extends TextView {public RecordsListView(BitmapFont f,float y){super(f,0,y,"");}public void setRecords(List<Integer> r){StringBuilder s=new StringBuilder();for(int i=0;i<Math.min(5,r.size());i++)s.append(i+1).append(". ").append(r.get(i)).append("\n");setText(s.toString());x=(GameSettings.SCREEN_WIDTH-width)/2f;}}
