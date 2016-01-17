package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;

public class HealthComponent extends Component {

	public float maxHP;
	public float currentHP;
	
	public HealthComponent( float max )
	{
		this.maxHP = max;
		this.currentHP = max;
	}
	
}
