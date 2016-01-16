package com.alesegdia.demux.components;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.ecs.Component;
import com.alesegdia.demux.ecs.Entity;

public class PickupEffectComponent extends Component {

	public List<Entity> pickupsCollectedLastFrame = new LinkedList<Entity>();
	
}
