package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsComponent extends Component {
	
	public Body body;
	public boolean grounded;
	public Vector2 boxOffset = new Vector2(0,0);

}
