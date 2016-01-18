package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;

public class GaugeComponent extends Component {

	public float maxGauge = 300;
	public float currentGauge = 300;
	public float regenRate = 1f;
	//public float regenRate = 40f;
	
	public CharSequence gauge() {
		return "" + Math.round(currentGauge * 100 / maxGauge) + "%";
	}
	
}
