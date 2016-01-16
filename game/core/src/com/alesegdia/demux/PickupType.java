package com.alesegdia.demux;

import com.alesegdia.troidgen.util.RNG;

public enum PickupType {
	HEAL,
	INC_SP, INC_STAMINA, INC_GAUGE,
	ABILITY_DASH, ABILITY_SJUMP,
	TRI_MOD, SINE_MOD, DEMUX_MOD, BI_MOD;
	
	public static PickupType MakeRandom()
	{
		PickupType pt = null;
		switch(RNG.rng.nextInt(0,9))
		{
		case 0: pt = PickupType.ABILITY_DASH; break;
		case 1: pt = PickupType.ABILITY_SJUMP; break;
		case 2: pt = PickupType.BI_MOD; break;
		case 3: pt = PickupType.DEMUX_MOD; break;
		case 4: pt = PickupType.HEAL; break;
		case 5: pt = PickupType.INC_GAUGE; break;
		case 6: pt = PickupType.INC_SP; break;
		case 7: pt = PickupType.INC_STAMINA; break;
		case 8: pt = PickupType.SINE_MOD; break;
		case 9: pt = PickupType.TRI_MOD; break;
		}
		return pt;
	}
}