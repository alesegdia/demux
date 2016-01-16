package com.alesegdia.demux.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PickupLocations {
	public Array<Vector2> pickups = new Array<Vector2>();
	public Array<Vector2> abilityPickups = new Array<Vector2>();
	
	@Override
	public String toString()
	{
		String str = "pickups (" + pickups.size + ")\n";
		for( Vector2 v2 : pickups )
		{
			str += v2 + ", ";
		}
		str += "ability pickups (" + abilityPickups.size + ") \n";
		for( Vector2 v2 : abilityPickups )
		{
			str += v2 + ", ";
		}
		
		return str;
	}
}