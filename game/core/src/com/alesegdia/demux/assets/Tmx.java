package com.alesegdia.demux.assets;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Tmx {

	private static Map<String, TilemapWrapper> mapHash;
	
	public static void Initialize()
	{
		mapHash = new Hashtable<String, TilemapWrapper>();
		FileHandle dirHandle = Gdx.files.internal("./maps/");
		for( FileHandle entry : dirHandle.list() )
		{
			String ext = entry.extension();
			if( ext.charAt(0) == 't' && ext.charAt(1) == 'm' && ext.charAt(2) == 'x' )
			{
				mapHash.put(entry.nameWithoutExtension(), new TilemapWrapper(entry.path(), entry.nameWithoutExtension()));
			}
		}
	}
	
	public static List<Room> GetRoomsOfType( RoomType rtype )
	{
		List<Room> lr = new LinkedList<Room>();
		for( TilemapWrapper tmw : mapHash.values() )
		{
			Room r = tmw.createRoom();
			System.out.println(r.rtype);
			if( r.rtype == rtype )
			{
				lr.add(r);
			}
		}
		return lr;
	}
	
	public static List<Room> GetRoomsOfTypeFitting( RoomType rtype, RestrictionSet rs )
	{
		List<Room> lr = new LinkedList<Room>();
		for( TilemapWrapper tmw : mapHash.values() )
		{
			Room r = tmw.createRoom();
			if( r.rtype == rtype && r.rinfo.restriction.resolves(rs) )
			{
				lr.add(r);
			}
		}
		return lr;
	}

	public static List<Room> GetRoomsOfTypeAndRestriction( RoomType rtype, RestrictionSet rs )
	{
		List<Room> lr = new LinkedList<Room>();
		for( TilemapWrapper tmw : mapHash.values() )
		{
			Room r = tmw.createRoom();
			if( r.rtype == rtype && r.rinfo.restriction.equals(rs) )
			{
				lr.add(r);
			}
		}
		return lr;
	}

	public static TilemapWrapper GetMap(String string) {
		return mapHash.get(string);
	}
	
}
