package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.troidgen.room.Link;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class PlayerComponent extends Component{

	public boolean jumping;
	public boolean isPressingDown;
	public boolean prevWasSolid;
	public Link gotoRoom = null;
	public Vector2 spawnPos;
	public Entity linkEntity = null;
	public boolean superJump;
	public Body platform = null;
	
	public boolean showMap = false;
	public int accumulatedAbilityPoints = 18;
	public Array<Body> platforms = new Array<Body>();
}
