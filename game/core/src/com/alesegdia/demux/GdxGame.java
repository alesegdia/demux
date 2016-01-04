package com.alesegdia.demux;

import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.assets.TmxRoomInfoLoader;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.screen.SelectMapScreen;
import com.alesegdia.demux.screen.GameplayScreen;
import com.alesegdia.troidgen.GraphBuilder;
import com.alesegdia.troidgen.IRoomProvider;
import com.alesegdia.troidgen.LayoutBuilder;
import com.alesegdia.troidgen.LayoutBuilderConfig;
import com.alesegdia.troidgen.LinkBuilder;
import com.alesegdia.troidgen.ManualRoomProvider;
import com.alesegdia.troidgen.MinSizeRoomGroupValidator;
import com.alesegdia.troidgen.OverlapSolverConfig;
import com.alesegdia.troidgen.RandomRoomProvider;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomInfo;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Rect;
import com.alesegdia.troidgen.util.UpperMatrix2D;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera cam;
	
	public SelectMapScreen gameScreen;
	public GameplayScreen tilemapScreen;
	public Physics physics;

	@Override
	public void create () {
		batch = new SpriteBatch();
		
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(12, 12 * (h / w));
        cam.setToOrtho(false, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        Tmx.Initialize();
        Gfx.Initialize();
        
        ManualRoomProvider mrp = new ManualRoomProvider();
        mrp.addAll(Tmx.GetRoomsOfType(RoomType.COMMON));
        
		LayoutBuilder lb = new LayoutBuilder();
		LayoutBuilderConfig lbc = new LayoutBuilderConfig();
		lbc.spawnRect = new Rect(-30, -30, 30, 30);
		lbc.numIterations = 20;
		
		OverlapSolverConfig osc = new OverlapSolverConfig();
		osc.separationParameter = 1f;
		osc.enableTweakNearSeparation = false;
		osc.resolution = 64;
		osc.enclosingRect = new Rect(-20, -15, 40, 30);
		
		lbc.osc = osc;
		lbc.spawnRect = new Rect(-8, -8, 16, 16);
		
		MinSizeRoomGroupValidator msrge = new MinSizeRoomGroupValidator( 6 );

		RestrictionSet rs = new RestrictionSet(4, true, true, true, true);
		List<Room> result = lb.generate(lbc, mrp, msrge, rs);
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		System.out.println(m);
		
		System.out.println(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);

		RectDebugger rd = new RectDebugger(result, 800, 600, osc.enclosingRect);
		rd.Show();
		
		physics = new Physics();

        gameScreen = new SelectMapScreen(this);
        tilemapScreen = new GameplayScreen(this, Tmx.GetMap("common_2x1"));
        setScreen(tilemapScreen);
	}

	@Override
	public void render () {
		super.render();
	}

}
