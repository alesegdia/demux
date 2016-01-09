package com.alesegdia.demux.map;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.troidgen.ExactRoomProvider;
import com.alesegdia.troidgen.ExactRoomProviderValidator;
import com.alesegdia.troidgen.GraphBuilder;
import com.alesegdia.troidgen.IRoomProvider;
import com.alesegdia.troidgen.IWorldComposer;
import com.alesegdia.troidgen.LayoutBuilder;
import com.alesegdia.troidgen.LayoutBuilderConfig;
import com.alesegdia.troidgen.LinkBuilder;
import com.alesegdia.troidgen.MinSizeRoomGroupValidator;
import com.alesegdia.troidgen.OverlapSolverConfig;
import com.alesegdia.troidgen.RandomRoomProvider;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Logger;
import com.alesegdia.troidgen.util.Rect;
import com.alesegdia.troidgen.util.UpperMatrix2D;

public class MultipleConstraintComposer implements IWorldComposer {
	
	@Override
	public List<Room> compose(List<Room> fixed) {

        
		LayoutBuilder lb = new LayoutBuilder();
		LayoutBuilderConfig lbc = new LayoutBuilderConfig();
		lbc.spawnRect = new Rect(-30, -30, 30, 30);
		lbc.numIterations = 20;
		
		OverlapSolverConfig osc = new OverlapSolverConfig();
		osc.separationParameter = 1f;
		osc.enableTweakNearSeparation = true;
		osc.resolution = 1;
		osc.enclosingRect = new Rect(-20, -15, 150, 150);
		//osc.enclosingRect = new Rect(-10, -10, 20, 20);
		//osc.enclosingRect = new Rect(0, 0, 15, 15);
		//osc.enclosingRect = new Rect(0, 0, 1, 1);
		
		lbc.osc = osc;
		lbc.spawnRect = new Rect(-8, -8, 30, 30);
		//lbc.spawnRect = new Rect(-10, -10, 20, 20);
		//lbc.spawnRect = new Rect(0, 0, 30, 30);
		
		RestrictionSet rs1 = new RestrictionSet(4, false, false, false, false);
		RestrictionSet rs2 = new RestrictionSet(4, true, false, false, false);
		
		ExactRoomProvider mrp = new ExactRoomProvider();
		//mrp.addAll(Tmx.GetRoomsOfType(RoomType.COMMON));
		mrp.addAll(Tmx.GetRoomsOfTypeFitting(RoomType.COMMON, rs1));
        
		ExactRoomProviderValidator msrge = new ExactRoomProviderValidator( mrp );
		List<Room> result = lb.generate(lbc, mrp, msrge);
		List<Room> tmp2 = new LinkedList<Room>();
		tmp2.addAll(result);

		float pos = 0f;
		float k = 0.0001f;
		
		while( result.size() < 40 )
		{
			//if( pos > 10 ) k = -k;
			//if( pos < -10 ) k = -k;
			//pos += k;
			//lbc.spawnRect.position.x -= k;
			//osc.enclosingRect.position.x -= k;
			mrp = new ExactRoomProvider();
			//mrp.addAll(Tmx.GetRoomsOfTypeAndRestriction(RoomType.COMMON, rs1));
			mrp.addAll(Tmx.GetRoomsOfTypeFitting(RoomType.COMMON, rs1));
			msrge = new ExactRoomProviderValidator( mrp );
			//result = addNotAdded(result, tmp2);
			result = lb.generate(lbc, mrp, msrge, result);
		}

		Logger.Log(result.size());
		
		List<Room> tmp = new LinkedList<Room>();
		tmp.addAll(result);
		
		while( result.size() < 80 )
		{
			Logger.Log(lbc.spawnRect.position);
			lbc.spawnRect.position.x -= k;
			lbc.spawnRect.position.y -= k;
			osc.enclosingRect.position.x -= k;
			osc.enclosingRect.position.y -= k;
			mrp = new ExactRoomProvider();
			mrp.addAll(Tmx.GetRoomsOfTypeAndRestriction(RoomType.COMMON, rs2));
			//mrp.addAll(Tmx.GetRoomsOfTypeFitting(RoomType.COMMON, rs2));
			msrge = new ExactRoomProviderValidator( mrp );
			result = addNotAdded(result, tmp);
			Logger.Log("enter");
			result = lb.generate(lbc, mrp, msrge, result);
			Logger.Log("quit");
			if( false ) //result.size() > 40 )
			{
				(new RectDebugger(result, 800, 600)).Show();
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/*
		{
			lbc.spawnRect.position.x += k;
			mrp = new ExactRoomProvider();
			mrp.addAll(Tmx.GetRoomsOfTypeAndRestriction(RoomType.COMMON, rs1));
			msrge = new ExactRoomProviderValidator( mrp );
			result = lb.generate(lbc, mrp, msrge, result);
		}

		{
			lbc.spawnRect.position.y -= k;
			mrp = new ExactRoomProvider();
			mrp.addAll(Tmx.GetRoomsOfTypeAndRestriction(RoomType.COMMON, rs1));
			msrge = new ExactRoomProviderValidator( mrp );
			result = lb.generate(lbc, mrp, msrge, result);
		}

		{
			lbc.spawnRect.position.y += k;
			mrp = new ExactRoomProvider();
			mrp.addAll(Tmx.GetRoomsOfTypeAndRestriction(RoomType.COMMON, rs1));
			msrge = new ExactRoomProviderValidator( mrp );
			result = lb.generate(lbc, mrp, msrge, result);
		}

		*/
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);

		RectDebugger rd = new RectDebugger(result, 800, 600, osc.enclosingRect);
		rd.Show();
		
		return result;
	}

	private List<Room> addNotAdded(List<Room> result, List<Room> tmp) {
		for( Room r : tmp )
		{
			if( !result.contains(r) )
			{
				result.add(r);
			}
		}
		return result;
	}

}
