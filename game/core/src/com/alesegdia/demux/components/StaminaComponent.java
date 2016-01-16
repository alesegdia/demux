package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;

public class StaminaComponent extends Component {

	public float regenRate = 0.1f;
	public float max = 300f;
	public float current = 300f;
	public CharSequence stamina() {
		return "" + Math.round(current * 100 / max) + "%";
	}
	
}
