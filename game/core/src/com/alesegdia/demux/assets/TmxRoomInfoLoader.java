package com.alesegdia.demux.assets;

import com.alesegdia.troidgen.room.RoomInfo;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Vec2;

import java.util.LinkedList;
import java.util.List;

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
		
		/**
		 * constraint: 1001110 -> RestrictionSet
		 * links: [X1-Y1-D1 ... XN-YN-D2]
		 * 		- XN: link relative x coordinate
		 * 		- YN: link relative y coordinate
		 * 		- DN: possible link sides
		 * size: 4x2 -> size in blocks of tiles
		 * type:
		 *		- passage: special room with passage from one RestrictionSet to another
		 *		- ability: special room with an ability needed to resolve one restriction
		 *				   and travel through a passage to a next zone
		 *		- common: normal transition room
		 */

		// block size
		rinfo.size = parseSize((String) props.get("size"));
		
		// links
		rinfo.linkInfo = parseLinks((String) props.get("links"));
		
		// parse constraints
		rinfo.restriction = RestrictionSet.FromString((String) props.get("constraint"));
		
		// parse room type
		rinfo.rtype = RoomType.FromString((String) props.get("type"));
		
		if( rinfo.rtype == RoomType.PASSAGE )
		{
			rinfo.passageNextRestriction = RestrictionSet.FromString((String) props.get("needed-constraint"));
		}
		else if( rinfo.rtype == RoomType.ABILITY )
		{
			rinfo.constraintSolved = Integer.parseInt((String) props.get("constraint-solved"));
		}
		
		System.out.println(rinfo);
		
		return rinfo;
	}

	private List<LinkInfo> parseLinks(String links) {
		List<LinkInfo> retlist = new LinkedList<LinkInfo>();
		String[] linkStr = links.split(" ");
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
					retlist.add(linfo);
				}
			}
		}
		return retlist;
	}

	private Vec2 parseSize(String szstr) {
		Vec2 size = new Vec2(0,0);
		String[] sizes = szstr.split("x");
		size.x = Float.parseFloat(sizes[0]);
		size.y = Float.parseFloat(sizes[1]);
		return size;
	}

	private boolean isValidSide(String side) {
		if( side.length() == 0 ) return false;
		char at = side.charAt(0);
		return at == 'T' || at == 'B' || at == 'L' || at == 'R';
	}

}
