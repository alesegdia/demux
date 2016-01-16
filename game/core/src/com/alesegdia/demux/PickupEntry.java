package com.alesegdia.demux;

import com.badlogic.gdx.math.Vector2;

public class PickupEntry {

	public PickupEntry(Vector2 p, PickupType pickupType) {
		this.position = new Vector2(p);
		this.pickupType = pickupType;
	}

	public Vector2 position;
	public PickupType pickupType;
	public boolean collected = false;
	
}
