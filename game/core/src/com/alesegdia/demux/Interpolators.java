package com.alesegdia.demux;

import com.alesegdia.troidgen.util.IInterpolator;
import com.alesegdia.troidgen.util.Lerper;

public class Interpolators {

	static private Interpolators instance = null;
	
	public IInterpolator speed = new Lerper(6f, 30f);
	public IInterpolator ttl = new Lerper(0.2f, 1f);
	public IInterpolator rate = new Lerper(0.01f, 1.f);
	public IInterpolator power = new Lerper(2f, 10f);
	
	public static Interpolators Instance()
	{
		if( instance == null )
		{
			instance = new Interpolators();
		}
		return instance;
	}
	
}
