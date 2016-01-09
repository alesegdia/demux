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
import com.alesegdia.troidgen.ManualRoomProvider;
import com.alesegdia.troidgen.MinSizeRoomGroupValidator;
import com.alesegdia.troidgen.OverlapSolverConfig;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.room.RoomType;
import com.alesegdia.troidgen.util.Rect;
import com.alesegdia.troidgen.util.UpperMatrix2D;

public class SingleConstraintComposer implements IWorldComposer {

	@Override
	public List<Room> compose(List<Room> fixed) {
		ExactRoomProvider erp = new ExactRoomProvider();
		//ManualRoomProvider erp = new ManualRoomProvider();
        erp.addAll(Tmx.GetRoomsOfType(RoomType.COMMON));
        //erp.add(Tmx.GetMap("0000-common_1x1-a").createRoom());
        
		LayoutBuilder lb = new LayoutBuilder();
		LayoutBuilderConfig lbc = new LayoutBuilderConfig();
		lbc.numIterations = 20;
		
		OverlapSolverConfig osc = new OverlapSolverConfig();
		osc.separationParameter = 1f;
		osc.enableTweakNearSeparation = false;
		osc.enableRandomDisplacement = true;
		osc.resolution = 1;
		lbc.osc = osc;

		osc.enclosingRect = new Rect(-7, -7, 14, 14);
		lbc.spawnRect = new Rect(-8, -8, 16, 16);

		osc.enclosingRect = new Rect(-10, -10, 20, 20);
		lbc.spawnRect = new Rect(-7, -7, 14, 14);
		
		osc.enclosingRect = new Rect(-30, -30, 60, 60);
		lbc.spawnRect = new Rect(-10, -10, 20, 20);
		
		ExactRoomProviderValidator erpv = new ExactRoomProviderValidator( erp );

		//MinSizeRoomGroupValidator erpv = new MinSizeRoomGroupValidator(50);
		
		List<Room> result = lb.generate(lbc, erp, erpv, fixed);

		GraphBuilder gb = new GraphBuilder();
		UpperMatrix2D<Float> m = gb.build(result);
		
		LinkBuilder linksb = new LinkBuilder();
		linksb.generate(result);

		RectDebugger rd = new RectDebugger(result, 800, 600, osc.enclosingRect);
		rd.Show();

		return result;
	}

}
