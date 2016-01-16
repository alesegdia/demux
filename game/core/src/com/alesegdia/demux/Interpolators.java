package com.alesegdia.demux;

import com.alesegdia.demux.interp.Cubic;
import com.alesegdia.demux.interp.Interpolator;
import com.alesegdia.demux.interp.Lerper;
import com.alesegdia.demux.interp.Square;

public class Interpolators {

	static private Interpolators instance = null;
	
	public Interpolator speed = new Lerper(6f, 30f);
	public Interpolator ttl = new Lerper(0.2f, 0.5f);
	public Interpolator rate = new Lerper(0.1f, .5f);
	public Interpolator power = new Lerper(2f, 10f);
	
	public static Interpolators Instance()
	{
		if( instance == null )
		{
			instance = new Interpolators();
		}
		return instance;
	}
	
}
