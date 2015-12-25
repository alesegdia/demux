package com.alesegdia.demux.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Tmx {

	public static TiledMap sampleMap;
	public static OrthogonalTiledMapRenderer sampleMapRenderer;
	
	public static void Initialize()
	{
		sampleMap = new TmxMapLoader().load("untitled.tmx");
		float unitScale = 1.f / 16.f;
		sampleMapRenderer = new OrthogonalTiledMapRenderer(sampleMap, unitScale);
	}
	
}
