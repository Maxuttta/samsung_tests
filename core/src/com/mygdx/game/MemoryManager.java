package com.mygdx.game;
import com.badlogic.gdx.Gdx; import com.badlogic.gdx.Preferences; import java.util.*;
public class MemoryManager {
    private static Preferences p(){return Gdx.app.getPreferences("space-cleaner");}
    public static boolean loadIsMusicOn(){return p().getBoolean("music",true);} public static boolean loadIsSoundOn(){return p().getBoolean("sound",true);}
    public static void saveMusicSettings(boolean v){p().putBoolean("music",v).flush();} public static void saveSoundSettings(boolean v){p().putBoolean("sound",v).flush();}
    public static List<Integer> loadTableRecords(){List<Integer> r=new ArrayList<Integer>(); String s=p().getString("records",""); if(!s.isEmpty())for(String x:s.split(","))try{r.add(Integer.parseInt(x));}catch(Exception ignored){} Collections.sort(r,Collections.reverseOrder()); return r;}
    public static void saveTableRecords(List<Integer> r){Collections.sort(r,Collections.reverseOrder()); StringBuilder s=new StringBuilder(); for(int i=0;i<Math.min(5,r.size());i++){if(i>0)s.append(',');s.append(r.get(i));}p().putString("records",s.toString()).flush();}
}
