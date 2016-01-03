package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.math.Vector2;

public class LinearVelocityComponent extends Component {

	public Vector2 linearVelocity = new Vector2(0,0);
	public Vector2 speed = new Vector2(0,0);
	
	public Vector2 cap = new Vector2(0,0);
	//						   top    bot    left   right
	public boolean doCap[] = { false, false, false, false };
	
}
