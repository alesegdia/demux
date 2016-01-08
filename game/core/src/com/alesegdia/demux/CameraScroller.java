package com.alesegdia.demux;

import com.alesegdia.demux.components.TransformComponent;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class CameraScroller {

	private Camera cam;
	private TiledMap tilemap;
	private TransformComponent playerTransform;
	private float mapWidth;
	private float mapHeight;
	private boolean scrollHoriz;
	private boolean scrollVert;
	private float vh;
	private float vw;

	public CameraScroller( Camera cam, TiledMap tm, TransformComponent tc )
	{
		this.cam = cam;
		this.tilemap = tm;
		this.playerTransform = tc;
		this.mapWidth = (Integer) (tm.getProperties().get("width"));
		this.mapHeight = (Integer) (tm.getProperties().get("height"));
		this.vw = GameConfig.VIEWPORT_WIDTH * 2;
		this.vh = GameConfig.VIEWPORT_HEIGHT * 2;
		this.scrollVert = vh < mapHeight;
		this.scrollHoriz = vw < mapWidth;
	}
	
	public void step()
	{
		if( !this.scrollVert )
		{
			cam.position.y = mapHeight/4f;
		}
		if( !this.scrollHoriz )
		{
			cam.position.x = mapWidth/4f;
		}
	}
	
}
