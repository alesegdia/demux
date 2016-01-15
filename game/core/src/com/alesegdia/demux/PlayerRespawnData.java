package com.alesegdia.demux;

import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.badlogic.gdx.math.Vector2;

public class PlayerRespawnData {

	public PlayerComponent plc = null;
	public Vector2 prevlvc = null;
	public Vector2 spawnPos = null;
	public AttackComponent ac;
	public ShootComponent sc;
	public WeaponComponent wc;

}
