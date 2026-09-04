package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends ApplicationAdapter {
    private static final float W=1280,H=720,GRAVITY=-1450;
    private SpriteBatch batch; private BitmapFont font; private Texture pixel, heroTex, enemyTex, diamondTex, crateTex, flagTex, bgTex, cloudsTex, reflectionsTex;
    private TextureRegion hero, enemy, diamond, crate, flag, bg, clouds, reflections;
    private Music music; private Sound destroySound, attackSound; private boolean musicEnabled, soundEnabled, russian;
    private TextureAtlas uiAtlas; private TextureRegion uiButton, uiPanel, uiLeft, uiRight, uiAttack;
    private TextureRegion[][] heroFrames, enemyFrames, diamondFrames, crateFrames, flagFrames;
    private TiledMap tiledMap; private OrthogonalTiledMapRenderer mapRenderer; private int mapHeight, mapWidth;
    private ExtendViewport viewport; private OrthographicCamera camera; private SaveService saveService; private InputController inputController; private LevelResourceManager levelResources;
    private enum State { MENU, SETTINGS, PLAYING, PAUSED, ENDED } private State state=State.MENU;
    private int selected, unlocked, level, score, lives; private float px,py,vx,vy,time,attack,hurt,deathTime; private boolean ground, right=true, playerDead; private String endText="";
    private Rectangle player=new Rectangle(), finish=new Rectangle(); private Array<Rectangle> floors=new Array<Rectangle>(), ladders=new Array<Rectangle>(); private Array<Item> gems=new Array<Item>(), boxes=new Array<Item>(); private Array<Enemy> foes=new Array<Enemy>();
    private static class Item { Rectangle r; boolean gone; float breaking=-1; Item(float x,float y,float w,float h){r=new Rectangle(x,y,w,h);} }
    private static class Enemy { Rectangle r, hit; float lo,hi,dir=1,vy; boolean grounded, defeated; Enemy(float x,float y,float lo,float hi){r=new Rectangle(x,y,34,30);hit=new Rectangle();this.lo=lo;this.hi=hi;syncHit();}void syncHit(){hit.set(r.x+5,r.y+2,24,25);} }

    @Override public void create(){
        batch=new SpriteBatch();FreeTypeFontGenerator generator=new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Montserrat-Bold.ttf"));FreeTypeFontGenerator.FreeTypeFontParameter parameter=new FreeTypeFontGenerator.FreeTypeFontParameter();parameter.size=22;parameter.characters=FreeTypeFontGenerator.DEFAULT_CHARS+"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";font=generator.generateFont(parameter);generator.dispose();font.getData().setScale(1.25f); camera=new OrthographicCamera(); viewport=new ExtendViewport(W,H,camera);
        Pixmap p=new Pixmap(1,1,Pixmap.Format.RGBA8888);p.setColor(Color.WHITE);p.fill();pixel=new Texture(p);p.dispose();
        String root="assets_for_pirate_game/texture/";
        heroTex=new Texture(root+"player/hero-tileset.png"); enemyTex=new Texture(root+"enemy/enemy-tileset.png"); diamondTex=new Texture(root+"items/diamond-tileset.png"); crateTex=new Texture(root+"items/bonus-tileset.png");flagTex=new Texture(root+"items/finish-line-tileset.png");bgTex=new Texture(root+"background/background.png");cloudsTex=new Texture(root+"background/big-clouds.png");
        reflectionsTex=new Texture(root+"background/reflections-tileset.png");
        heroFrames=TextureRegion.split(heroTex,64,40); enemyFrames=TextureRegion.split(enemyTex,34,30); diamondFrames=TextureRegion.split(diamondTex,24,24); crateFrames=TextureRegion.split(crateTex,32,32); flagFrames=TextureRegion.split(flagTex,34,93);
        hero=heroFrames[4][0]; enemy=enemyFrames[0][0];diamond=diamondFrames[0][0];crate=crateFrames[0][0];flag=flagFrames[0][0];bg=new TextureRegion(bgTex);clouds=new TextureRegion(cloudsTex);reflections=new TextureRegion(reflectionsTex);
        uiAtlas=new TextureAtlas("assets_for_pirate_game/skin/skin.atlas");uiButton=uiAtlas.findRegion("button-up-up.9");uiPanel=uiAtlas.findRegion("window.9");uiLeft=uiAtlas.findRegion("button-back-up.9");uiRight=uiAtlas.findRegion("button-forward-up.9");uiAttack=uiAtlas.findRegion("butotn-attcak-up.9");
        saveService=new SaveService("pirate-treasure");inputController=new InputController();levelResources=new LevelResourceManager();unlocked=saveService.unlockedLevel();musicEnabled=saveService.musicEnabled();soundEnabled=saveService.soundEnabled();russian=saveService.russian();music=Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/background_music.mp3"));destroySound=Gdx.audio.newSound(Gdx.files.internal("assets/sounds/destroy.mp3"));attackSound=Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shoot.mp3"));music.setLooping(true);if(musicEnabled)music.play();
    }
    @Override public void render(){float dt=Math.min(Gdx.graphics.getDeltaTime(),.033f);time+=dt;if(state==State.PLAYING)update(dt);Gdx.gl.glClearColor(.10f,.22f,.34f,1);Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);viewport.apply();if(state==State.MENU||state==State.SETTINGS){camera.zoom=1;camera.position.set(W/2,H/2,0);camera.update();batch.setProjectionMatrix(camera.combined);batch.begin();background();if(state==State.MENU)menu();else settings();batch.end();return;}drawMapWorld();camera.zoom=1;camera.position.set(W/2,H/2,0);camera.update();batch.setProjectionMatrix(camera.combined);batch.begin();hud();if(state==State.PAUSED)pauseMenu();if(state==State.ENDED)ended();batch.end();}
    void update(float dt){
        if(playerDead){deathTime+=dt;if(deathTime>=.7f)finish(false,endText);return;}
        boolean l=down(Input.Keys.A,Input.Keys.LEFT)||touch(25,25,100,85),r=down(Input.Keys.D,Input.Keys.RIGHT)||touch(140,25,100,85);boolean j=Gdx.input.isKeyJustPressed(Input.Keys.W)||Gdx.input.isKeyJustPressed(Input.Keys.UP)||touch(W-130,25,100,85);boolean a=Gdx.input.isKeyJustPressed(Input.Keys.F)||touch(W-245,25,100,85);
        boolean onLadder=false;for(Rectangle ladder:ladders)if(player.overlaps(ladder)){onLadder=true;break;}boolean up=down(Input.Keys.W,Input.Keys.UP)||touch(W-130,25,100,85),downKey=down(Input.Keys.S,Input.Keys.DOWN);
        if(a&&attack<=0){attack=.25f;if(soundEnabled)attackSound.play(.35f);}attack-=dt;hurt-=dt;float s=attack>0?125:260;if(l){vx=-s;right=false;}else if(r){vx=s;right=true;}else vx=MathUtils.lerp(vx,0,.23f);
        if(j&&ground&&!onLadder&&hurt<=0){vy=590;ground=false;}if(onLadder&&(up||downKey)){vy=(up?150:-150);ground=false;}else if(!onLadder)vy+=GRAVITY*dt;else vy=0;
        px+=vx*dt;player.setPosition(px,py);horizontal();py+=vy*dt;player.setPosition(px,py);ground=false;vertical();player.setPosition(px,py);
        for(Item i:gems)if(!i.gone&&player.overlaps(i.r)){i.gone=true;score+=50;}
        for(Item i:boxes){if(i.breaking>=0){i.breaking+=dt;if(i.breaking>=.48f)i.gone=true;}else if(!i.gone&&player.overlaps(i.r)){i.breaking=0;score+=30;if(soundEnabled)destroySound.play(.45f);}}
        Rectangle strike=new Rectangle(right?px+player.width:px-30,py+7,30,24);
        for(Enemy e:foes){if(e.defeated)continue;moveEnemy(e,dt);if(attack>0&&strike.overlaps(e.hit)){e.defeated=true;score+=10;if(soundEnabled)destroySound.play(.35f);}else if(player.overlaps(e.hit)&&hurt<=0){if(--lives<=0){startDeath("Don't touch enemies.\nThey don't like it.");return;}hurt=.9f;vx=px<e.r.x?-360:360;}}
        if(player.overlaps(finish)){finish(true,"Our congratulations!");return;}if(py<-80){startDeath("Don't fall into the pit!");return;}if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)||Gdx.input.isKeyJustPressed(Input.Keys.P))state=State.PAUSED;
    }
    boolean down(int a,int b){return inputController.pressed(a,b);} void horizontal(){for(Rectangle f:floors)if(player.overlaps(f)){px=vx>0?f.x-player.width:f.x+f.width;vx=0;player.setPosition(px,py);}}void vertical(){for(Rectangle f:floors)if(player.overlaps(f)){if(vy<=0){py=f.y+f.height;ground=true;}else py=f.y-player.height;vy=0;player.setPosition(px,py);}}
    void moveEnemy(Enemy e,float dt){
        e.grounded=false;e.vy+=GRAVITY*dt;e.r.y+=e.vy*dt;for(Rectangle f:floors)if(e.r.overlaps(f)&&e.vy<=0){e.r.y=f.y+f.height;e.vy=0;e.grounded=true;}
        if(e.hi>e.lo){float next=e.r.x+e.dir*80*dt;if(next<=e.lo||next>=e.hi)e.dir*=-1;else if(e.grounded&&hasFloorAt(e,next+(e.dir>0?e.r.width:-2)))e.r.x=next;else if(e.grounded)e.dir*=-1;}e.syncHit();
    }
    boolean hasFloorAt(Enemy e,float x){for(Rectangle f:floors)if(x>=f.x&&x<=f.x+f.width&&Math.abs(e.r.y-(f.y+f.height))<7)return true;return false;}
    void start(int n){
        level=n;score=0;lives=3;attack=hurt=deathTime=0;playerDead=false;floors.clear();ladders.clear();gems.clear();boxes.clear();foes.clear();
        String path="assets_for_pirate_game/maps/level"+(n+1)+".tmx"; levelResources.load(path);tiledMap=levelResources.map();mapRenderer=levelResources.renderer();
        TiledMapTileLayer layer=(TiledMapTileLayer)tiledMap.getLayers().get("floorAndWalls");mapHeight=layer.getHeight()*32;mapWidth=layer.getWidth()*32;
        MapObjects colliders=tiledMap.getLayers().get("floorAndWallsCollider").getObjects();
        for(MapObject o:colliders)if(o instanceof RectangleMapObject){Rectangle r=((RectangleMapObject)o).getRectangle();floors.add(new Rectangle(r.x,r.y,r.width,r.height));}
        for(MapObject o:tiledMap.getLayers().get("interactiveObjects").getObjects())if(o instanceof RectangleMapObject){Rectangle r=((RectangleMapObject)o).getRectangle();String name=o.getName();if("diamond".equals(name)||"coin".equals(name))gems.add(new Item(r.x,r.y,r.width,r.height));else if("bonusBlock".equals(name))boxes.add(new Item(r.x,r.y,r.width,r.height));else if("ladder".equals(name))ladders.add(new Rectangle(r.x,r.y,r.width,r.height));else if("finishLine".equals(name))finish.set(r.x,r.y,r.width,r.height);}
        for(MapObject o:tiledMap.getLayers().get("actors").getObjects())if(o instanceof RectangleMapObject){Rectangle r=((RectangleMapObject)o).getRectangle();if("player".equals(o.getName())){px=r.x;py=r.y;player.setSize(34,38);}else if("enemyStar".equals(o.getName())){Object value=o.getProperties().get("walkLength");int tiles=value instanceof Number?((Number)value).intValue():value==null?0:Integer.parseInt(value.toString());foes.add(new Enemy(r.x,r.y,r.x,r.x+tiles*32));}}
        vx=vy=0;state=State.PLAYING;
    }
    void add(float x,float y,float w,float h){floors.add(new Rectangle(x,y,w,h));}void startDeath(String text){playerDead=true;deathTime=0;endText=text;vx=vy=0;}void finish(boolean won,String text){endText=text;if(won){unlocked=Math.max(unlocked,Math.min(2,level+1));saveService.unlockedLevel(unlocked);}state=State.ENDED;}
    void background(){batch.draw(bg,-200,0,W+400,H);for(int i=-1;i<5;i++)batch.draw(clouds,i*500-(time*14%500),340,500,170);rect(-200,0,W+400,105,new Color(.12f,.31f,.46f,1));for(int i=-1;i<8;i++)batch.draw(reflections,i*205-(time*30%205),126,205,40);}
    void menu(){center("PIRATE TREASURE",635,2.2f,new Color(.13f,.22f,.48f,1));panel(355,315,570,230);center(t("SELECT AN EXPEDITION","ВЫБЕРИТЕ УРОВЕНЬ"),505,1.15f,Color.WHITE);String[] ns={"1  FIRST FLIGHT","2  REVENGE OF PIT","3  THE EMPIRE STRIKES"};for(int i=0;i<3;i++)button(405,445-i*48,470,38,ns[i]+(i>unlocked?"  (-)":""),i<=unlocked?new Color(.88f,.53f,.30f,1):new Color(.35f,.35f,.40f,1));button(405,250,470,48,t("START THIS GAME","НАЧАТЬ ИГРУ"),new Color(.20f,.58f,.42f,1));button(405,185,220,42,t("EXIT","ВЫХОД"),new Color(.78f,.38f,.25f,1));button(655,185,220,42,t("SETTINGS","НАСТРОЙКИ"),new Color(.78f,.38f,.25f,1));if(Gdx.input.justTouched()){if(touch(405,445,470,38))selected=0;else if(touch(405,397,470,38)&&unlocked>=1)selected=1;else if(touch(405,349,470,38)&&unlocked>=2)selected=2;else if(touch(405,250,470,48))start(selected);else if(touch(655,185,220,42))state=State.SETTINGS;else if(touch(405,185,220,42))Gdx.app.exit();}}
    void settings(){center(t("SETTINGS","НАСТРОЙКИ"),620,2f,new Color(.13f,.22f,.48f,1));panel(425,185,430,320);center(t("Progress and audio","ПРОГРЕСС И ЗВУК"),465,1f,Color.WHITE);button(480,405,320,40,t("RESET LEVELS","СБРОСИТЬ УРОВНИ"),new Color(.78f,.38f,.25f,1));button(480,355,320,40,t("MUSIC: ","МУЗЫКА: ")+(musicEnabled?"ON":"OFF"),new Color(.20f,.58f,.42f,1));button(480,305,320,40,t("SFX: ","ЗВУКИ: ")+(soundEnabled?"ON":"OFF"),new Color(.20f,.58f,.42f,1));button(480,255,320,40,t("LANGUAGE: ","ЯЗЫК: ")+(russian?"RU":"EN"),new Color(.20f,.58f,.42f,1));button(480,205,320,40,t("HOME","В МЕНЮ"),new Color(.78f,.38f,.25f,1));if(Gdx.input.justTouched()){if(touch(480,405,320,40)){unlocked=0;saveService.unlockedLevel(0);}else if(touch(480,355,320,40)){musicEnabled=!musicEnabled;saveService.musicEnabled(musicEnabled);if(musicEnabled)music.play();else music.pause();}else if(touch(480,305,320,40)){soundEnabled=!soundEnabled;saveService.soundEnabled(soundEnabled);}else if(touch(480,255,320,40)){russian=!russian;saveService.russian(russian);}else if(touch(480,205,320,40))state=State.MENU;}}
    void drawMapWorld(){
        camera.zoom=Math.max(.42f,mapHeight/H);float cx=MathUtils.clamp(px+player.width/2,W*camera.zoom/2,mapWidth-W*camera.zoom/2);camera.position.set(cx,mapHeight/2,0);camera.update();
        batch.setProjectionMatrix(camera.combined);batch.begin();for(int i=-1;i<=1;i++)batch.draw(bg,cx-W*camera.zoom/2+i*900,0,900,300);for(int i=-1;i<=3;i++)batch.draw(clouds,cx-W*camera.zoom/2+i*448-(time*12%448),270,448,101);batch.end();
        mapRenderer.setView(camera);mapRenderer.render();batch.setProjectionMatrix(camera.combined);batch.begin();
        for(Item i:gems)if(!i.gone)batch.draw(diamondFrames[(int)(time*8)%4][0],i.r.x,i.r.y,i.r.width,i.r.height);
        for(Item i:boxes)if(!i.gone){int frame=i.breaking<0?0:Math.min(7,4+(int)(i.breaking/.12f));batch.draw(crateFrames[0][frame],i.r.x,i.r.y,i.r.width,i.r.height);}
        for(Enemy e:foes)if(!e.defeated)batch.draw(enemyFrames[e.hi>e.lo?1:0][(int)(time*9)%(e.hi>e.lo?6:8)],e.r.x,e.r.y,e.r.width,e.r.height);
        batch.draw(flagFrames[0][(int)(time*6)%9],finish.x,finish.y,finish.width,finish.height);
        TextureRegion heroFrame=heroFrames[4][(int)(time*6)%5];if(Math.abs(vx)>20)heroFrame=heroFrames[0][(int)(time*10)%6];if(!ground)heroFrame=heroFrames[1][(int)(time*8)%3];if(attack>0)heroFrame=heroFrames[3][(int)(time*12)%3];if(hurt>0)heroFrame=heroFrames[2][(int)(time*10)%4];if(playerDead)heroFrame=heroFrames[5][Math.min(3,(int)(deathTime*6))];
        float heroX=right?px-15:px+player.width+15;batch.draw(heroFrame,heroX,py-8,right?64:-64,40);
        if(attack>0)rect(right?px+player.width:px-30,py+7,30,24,new Color(1,1,1,.18f));batch.end();
    }
    void hud(){backgroundHud();text("Score: "+score,30,682,1.15f,Color.WHITE);text("Left Lives: "+lives,185,682,1.15f,Color.WHITE);text("Level "+(level+1)+"   WASD / arrows: move   F: attack",30,650,.75f,Color.WHITE);button(W-135,655,100,38,"PAUSE",new Color(.78f,.38f,.25f,1));controls();if(Gdx.input.justTouched()&&touch(W-135,655,100,38))state=State.PAUSED;}
    void backgroundHud(){}
    void controls(){iconButton(uiLeft,25,25,100,80);iconButton(uiRight,140,25,100,80);iconButton(uiAttack,W-245,25,100,80);iconButton(uiButton,W-130,25,100,80);}
    void pauseMenu(){shade();panel(410,250,460,230);center("GAME ON PAUSE",405,1.35f,Color.WHITE);button(470,300,190,48,"RESUME",new Color(.20f,.58f,.42f,1));button(680,300,130,48,"HOME",new Color(.78f,.38f,.25f,1));if(Gdx.input.justTouched()){if(touch(470,300,190,48))state=State.PLAYING;else if(touch(680,300,130,48))state=State.MENU;}}
    void ended(){shade();panel(380,230,520,280);center(endText.split("\\n")[0],420,1.25f,Color.WHITE);if(endText.contains("\n"))center(endText.split("\\n")[1],390,1.1f,Color.WHITE);center("Score: "+score,340,1f,Color.WHITE);button(535,260,210,48,"HOME",new Color(.20f,.58f,.42f,1));if(Gdx.input.justTouched()&&touch(535,260,210,48))state=State.MENU;}
    String t(String english,String russianText){return russian?russianText:english;}void rect(float x,float y,float w,float h,Color c){batch.setColor(c);batch.draw(pixel,x,y,w,h);batch.setColor(Color.WHITE);}void shade(){rect(0,0,W,H,new Color(0,0,0,.55f));}void panel(float x,float y,float w,float h){batch.draw(uiPanel,x,y,w,h);}float width(String s,float scale){return s.length()*8*scale;}void button(float x,float y,float w,float h,String s,Color c){batch.setColor(c);batch.draw(uiButton,x,y,w,h);batch.setColor(Color.WHITE);text(s,x+(w-width(s,1))/2,y+h/2+7,1,Color.WHITE);}void iconButton(TextureRegion region,float x,float y,float w,float h){batch.setColor(1,1,1,.82f);batch.draw(region,x,y,w,h);batch.setColor(Color.WHITE);}void text(String s,float x,float y,float sc,Color c){font.getData().setScale(sc);font.setColor(c);font.draw(batch,s,x,y);}void center(String s,float y,float sc,Color c){text(s,(W-width(s,sc))/2,y,sc,c);}boolean touch(float x,float y,float w,float h){return inputController.touched(new Rectangle(x,y,w,h),viewport);}
    @Override public void resize(int w,int h){viewport.update(w,h,true);}@Override public void dispose(){if(levelResources!=null)levelResources.unload();music.dispose();destroySound.dispose();attackSound.dispose();batch.dispose();font.dispose();pixel.dispose();uiAtlas.dispose();heroTex.dispose();enemyTex.dispose();diamondTex.dispose();crateTex.dispose();flagTex.dispose();bgTex.dispose();cloudsTex.dispose();reflectionsTex.dispose();}
}
