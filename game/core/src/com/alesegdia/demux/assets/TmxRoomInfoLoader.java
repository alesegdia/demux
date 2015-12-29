package com.alesegdia.demux.assets;

import java.util.Iterator;

import com.alesegdia.troidgen.room.RoomInfo;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Direction;
import com.alesegdia.troidgen.room.LinkInfo;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class TmxRoomInfoLoader {
	
	private static RestrictionSet[] restrictionSets = {
			new RestrictionSet( 4, false, false, false, false ),
			new RestrictionSet( 4, true, false, false, false ),
			new RestrictionSet( 4, true, true , false, false ),
			new RestrictionSet( 4, true, true , true , false ),
			new RestrictionSet( 4, true, true , true, true ),
	};

	public RoomInfo load(TiledMap tm)
	{
		MapProperties props = tm.getProperties();
		RoomInfo rinfo = new RoomInfo();
		
		// get block size
		String[] sizes = ((String) props.get("size")).split("x");
		rinfo.size.x = Float.parseFloat(sizes[0]);
		rinfo.size.y = Float.parseFloat(sizes[1]);
		
		// get links
		String[] linkStr = ((String) props.get("links")).split(" ");
		for( String s : linkStr )
		{
			String[] linkParts = s.split("-");
			int xblock = Integer.parseInt(linkParts[0]); 	// relative to block x coord
			int yblock = Integer.parseInt(linkParts[1]);	// relative to block y coord
			String[] sides = linkParts[2].split("");		// possible facing
			for( String side : sides )
			{
				if( isValidSide(side) )
				{
					LinkInfo linfo = new LinkInfo();
					linfo.relativeCoordinate.Set(xblock, yblock);
					linfo.direction = Direction.getFromChar(side.charAt(0));
					rinfo.linkInfo.add(linfo);
				}
			}
		}
		
		// parse constraints
		String constraintStr = (String) props.get("constraint");
		rinfo.restriction = new RestrictionSet(constraintStr.length());
		for( int i = 0; i < constraintStr.length(); i++ )
		{
			char bit = constraintStr.charAt(i);
			if( bit == '1' )
			{
				rinfo.restriction.set(i, true);
			}
			else if( bit == '0' )
			{
				rinfo.restriction.set(i, false);
			}
			else
			{
				assert(false);
			}
			//System.out.println("<" + bit + ">");
		}
		
		//System.out.println(rinfo.restriction);
		
		return rinfo;
	}

	private boolean isValidSide(String side) {
		if( side.length() == 0 ) return false;
		char at = side.charAt(0);
		return at == 'T' || at == 'B' || at == 'L' || at == 'R';
	}

}
