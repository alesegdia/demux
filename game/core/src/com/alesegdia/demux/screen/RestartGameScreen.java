package com.alesegdia.demux.screen;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.PickupEntry;
import com.alesegdia.demux.PickupType;
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

		// compose layout
		List<Room> result = composer.compose(new LinkedList<Room>());
		this.roomLayout = result;
		
		// builds an upper matrix representing connections
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		// connect rooms
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);
		
		// get start rooms group
		List<List<Room>> startRooms = computeStartRooms(result);
		IRoomGroupFilter rgf = new BiggestGroupFilter();
		List<Room> start = rgf.filter(startRooms);

		// pick one random room from the start group
		Util.shuffle(start);
		Room r = start.get(0);
		
		int total_pickups = 0;
		int total_abpickups = 0;
		
		List<Room> lvl0rooms = new LinkedList<Room>();
		List<Room> lvl1rooms = new LinkedList<Room>();
		
		for( Room rr : result )
		{
			if( rr.rinfo.restriction.equals(new RestrictionSet(4, false, false, false, false)) )
			{
				lvl0rooms.add(rr);
			}
			else
			{
				lvl1rooms.add(rr);
			}
		}
		
		for( Room rr : lvl0rooms )
		{
			TiledMap tm = Tmx.GetMap(rr.rinfo.id).tilemap;
			PickupLocations pl = MapPickupCollector.collect(tm, GameConfig.METERS_TO_PIXELS);
			total_pickups += pl.pickups.size;
			total_abpickups += pl.abilityPickups.size;				
		}
		
		List<PickupType> lvl0pickups = new LinkedList<PickupType>();
		lvl0pickups.add(PickupType.ABILITY_DASH);
		lvl0pickups.add(PickupType.ABILITY_SJUMP);
		lvl0pickups.add(PickupType.BI_MOD);
		lvl0pickups.add(PickupType.SINE_MOD);
		
		List<PickupType> lvl1pickups = new LinkedList<PickupType>();
		lvl1pickups.add(PickupType.TRI_MOD);
		lvl1pickups.add(PickupType.DEMUX_MOD);
		
		for( int i = 0; i < 5; i++ )
		{
			lvl0pickups.add(PickupType.INC_GAUGE);
			lvl1pickups.add(PickupType.INC_GAUGE);
			lvl0pickups.add(PickupType.INC_STAMINA);
			lvl1pickups.add(PickupType.INC_STAMINA);
		}
		
		for( int i = 0; i < 10; i++ )
		{
			lvl0pickups.add(PickupType.INC_SP);
			lvl1pickups.add(PickupType.INC_SP);
		}


		List<PickupEntry> lvl0entries = new LinkedList<PickupEntry>();
		List<PickupEntry> lvl1entries = new LinkedList<PickupEntry>();
		Hashtable<Room, List<PickupEntry>> lvl0hash = MapPickupCollector.buildRoomPickupHash( lvl0rooms, lvl0entries );
		Hashtable<Room, List<PickupEntry>> lvl1hash = MapPickupCollector.buildRoomPickupHash( lvl1rooms, lvl1entries );
		placePickups( lvl0pickups, lvl0entries );
		placePickups( lvl1pickups, lvl1entries );

		this.pickupMap = new Hashtable<Room, List<PickupEntry>>();
		this.pickupMap.putAll(lvl0hash);
		this.pickupMap.putAll(lvl1hash);
		
		for( Room rr : lvl1rooms )
		{
			rr.opened = false;
		}
		
        g.tilemapScreen.reset(r);
        g.setScreen(g.tilemapScreen);
	}

	private void placePickups(List<PickupType> lvl0pickups, List<PickupEntry> lvl0entries) {
		assert(lvl0pickups.size() <= lvl0entries.size());
		List<PickupEntry> entries = new LinkedList<PickupEntry>();
		List<PickupType> pickups = new LinkedList<PickupType>();
		entries.addAll(lvl0entries);
		pickups.addAll(lvl0pickups);
		
		System.out.println(entries.size());
		
		while( !pickups.isEmpty() )
		{
			PickupType pt = pickups.get(0);
			pickups.remove(0);
			
			Util.shuffle(entries);
			PickupEntry pe = entries.get(0);
			entries.remove(0);
			
			pe.pickupType = pt;
		}
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
