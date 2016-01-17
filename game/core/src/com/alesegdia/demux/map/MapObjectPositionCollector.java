package com.alesegdia.demux.map;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapObjectPositionCollector {

	public static Array<Vector2> Collect( Map m, String layerName, float ppt )
	{
		Array<Vector2> positions = new Array<Vector2>();
		
		MapObjects objects = m.getLayers().get(layerName).getObjects();
		for(MapObject object : objects)
		{
			if (object instanceof EllipseMapObject) {
				EllipseMapObject emo = ((EllipseMapObject)object);
				positions.add(new Vector2(emo.getEllipse().x, emo.getEllipse().y));
			}
		}
		
		return positions;
	}
	
}
