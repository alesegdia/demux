package com.alesegdia.demux.assets;

import java.util.Hashtable;
import java.util.Map;

import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Tmx {

	private static Map<String, TilemapWrapper> mapHash;
	
	public static void Initialize()
	{
		mapHash = new Hashtable<String, TilemapWrapper>();
		FileHandle dirHandle = Gdx.files.internal("./bin/maps/");
		for( FileHandle entry : dirHandle.list() )
		{
			String ext = entry.extension();
			if( ext.charAt(0) == 't' && ext.charAt(1) == 'm' && ext.charAt(2) == 'x' )
			{
				mapHash.put(entry.nameWithoutExtension(), new TilemapWrapper(entry.path()));
			}
		}
	}

	public static TilemapWrapper GetMap(String string) {
		return mapHash.get(string);
	}
	
}
