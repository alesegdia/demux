package com.alesegdia.demux.screen;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.PickupEntry;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.map.MapPickupCollector;
import com.alesegdia.demux.map.MultipleConstraintComposer;
import com.alesegdia.demux.map.PickupLocations;
import com.alesegdia.demux.map.SingleConstraintComposer;
import com.alesegdia.troidgen.BiggestGroupFilter;
import com.alesegdia.troidgen.GraphBuilder;
import com.alesegdia.troidgen.GroupExtractor;
import com.alesegdia.troidgen.IRoomGroupFilter;
import com.alesegdia.troidgen.IWorldComposer;
import com.alesegdia.troidgen.LinkBuilder;
import com.alesegdia.troidgen.RoomRestrictionValidator;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.util.UpperMatrix2D;
import com.alesegdia.troidgen.util.Util;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class RestartGameScreen implements Screen {

	private GdxGame g;
	List<Room> roomLayout;
	//private IWorldComposer composer = new SingleConstraintComposer();
	private IWorldComposer composer = new MultipleConstraintComposer();
	public Hashtable<Room, List<PickupEntry>> pickupMap;

	public RestartGameScreen(GdxGame gdxGame) {
		this.g = gdxGame;
	}

	private List<List<Room>> computeStartRooms(List<Room> result) {
		return new GroupExtractor().solve(result, new RoomRestrictionValidator(new RestrictionSet(4, false, false, false, false)));
	}
	
	@Override
	public void show() {

		List<Room> result = composer.compose(new LinkedList<Room>());
		this.roomLayout = result;
		
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);
		
		List<List<Room>> startRooms = computeStartRooms(result);

		IRoomGroupFilter rgf = new BiggestGroupFilter();
		
		List<Room> start = rgf.filter(startRooms);
		Util.shuffle(start);

		Room r = start.get(0);
		
		int total_pickups = 0;
		int total_abpickups = 0;
		for( Room rr : result )
		{
			TiledMap tm = Tmx.GetMap(rr.rinfo.id).tilemap;
			PickupLocations pl = MapPickupCollector.collect(tm, GameConfig.METERS_TO_PIXELS);
			total_pickups += pl.pickups.size;
			total_abpickups += pl.abilityPickups.size;
			System.out.println(pl);
		}
		System.out.println("total: " + total_pickups + ", " + total_abpickups + "\n");

		this.pickupMap = MapPickupCollector.buildRoomPickupHash( result );
		System.out.println(pickupMap);
		
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
