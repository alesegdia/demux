package com.alesegdia.demux.screen;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.troidgen.ExactRoomProvider;
import com.alesegdia.troidgen.ExactRoomProviderValidator;
import com.alesegdia.troidgen.GraphBuilder;
import com.alesegdia.troidgen.LayoutBuilder;
import com.alesegdia.troidgen.LayoutBuilderConfig;
import com.alesegdia.troidgen.LinkBuilder;
import com.alesegdia.troidgen.ManualRoomProvider;
import com.alesegdia.troidgen.MinSizeRoomGroupValidator;
import com.alesegdia.troidgen.OverlapSolverConfig;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Rect;
import com.alesegdia.troidgen.util.UpperMatrix2D;
import com.badlogic.gdx.Screen;

public class RestartGameScreen implements Screen {

	private GdxGame g;
	List<Room> roomLayout;

	public RestartGameScreen(GdxGame gdxGame) {
		this.g = gdxGame;
	}

	@Override
	public void show() {
        ExactRoomProvider mrp = new ExactRoomProvider();
        List<Room> rooms = new LinkedList<Room>();
        rooms.add(Tmx.GetMap("common_2x1").createRoom());
        //rooms.add(Tmx.GetMap("common_4x1").createRoom());
        mrp.addAll(rooms);
        
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
		
		ExactRoomProviderValidator msrge = new ExactRoomProviderValidator( mrp );

		List<Room> result = lb.generate(lbc, mrp, msrge);
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);

		RectDebugger rd = new RectDebugger(result, 800, 600, osc.enclosingRect);
		rd.Show();
		
		Room r = result.get(0);
		
		this.roomLayout = result;

        g.tilemapScreen.reset(r);
        g.setScreen(g.tilemapScreen);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
