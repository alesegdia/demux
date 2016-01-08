package com.alesegdia.demux.map;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.troidgen.ExactRoomProvider;
import com.alesegdia.troidgen.ExactRoomProviderValidator;
import com.alesegdia.troidgen.GraphBuilder;
import com.alesegdia.troidgen.IWorldComposer;
import com.alesegdia.troidgen.LayoutBuilder;
import com.alesegdia.troidgen.LayoutBuilderConfig;
import com.alesegdia.troidgen.LinkBuilder;
import com.alesegdia.troidgen.OverlapSolverConfig;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Rect;
import com.alesegdia.troidgen.util.UpperMatrix2D;

public class SingleConstraintComposer implements IWorldComposer {

	@Override
	public List<Room> compose(List<Room> fixed) {
        ExactRoomProvider mrp = new ExactRoomProvider();
        mrp.addAll(Tmx.GetRoomsOfType(RoomType.COMMON));
        
		LayoutBuilder lb = new LayoutBuilder();
		LayoutBuilderConfig lbc = new LayoutBuilderConfig();
		lbc.spawnRect = new Rect(-30, -30, 30, 30);
		lbc.numIterations = 20;
		
		OverlapSolverConfig osc = new OverlapSolverConfig();
		osc.separationParameter = 1f;
		osc.enableTweakNearSeparation = false;
		osc.resolution = 64;
		osc.enclosingRect = new Rect(-20, -15, 40, 30);
		
		lbc.osc = osc;
		lbc.spawnRect = new Rect(-8, -8, 16, 16);
		
		ExactRoomProviderValidator msrge = new ExactRoomProviderValidator( mrp );

		List<Room> result = lb.generate(lbc, mrp, msrge, fixed);
		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);

		RectDebugger rd = new RectDebugger(result, 800, 600, osc.enclosingRect);
		rd.Show();

		return result;
	}

}
