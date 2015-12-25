package com.alesegdia.demux.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Tmx {

	public static TilemapWrapper sampleMap;

	public static void Initialize()
	{
		sampleMap = new TilemapWrapper("untitled.tmx");
	}
	
}
