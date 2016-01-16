package com.alesegdia.demux.map;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MapPickupCollector {
	
	// The pixels per tile. If your tiles are 16x16, this is set to 16f
	private static float ppt = 8f;

	public static PickupLocations collect(Map map, float pixels) {
		ppt = pixels;
		PickupLocations pl = new PickupLocations();
		parseObjects( map.getLayers().get("pickups").getObjects(), pl.pickups );
		parseObjects( map.getLayers().get("ability-pickups").getObjects(), pl.abilityPickups );
		return pl;
	}

	static void parseObjects( MapObjects objects, Array<Vector2> storage )
	{
		for(MapObject object : objects)
		{
			if (object instanceof EllipseMapObject) {
				storage.add(handleCircle((EllipseMapObject) object));
			} else {
				continue;
			}
		}
	}

	private static Vector2 handleCircle(EllipseMapObject object) {
		return new Vector2(object.getEllipse().x / ppt, object.getEllipse().y / ppt);
	}

}

