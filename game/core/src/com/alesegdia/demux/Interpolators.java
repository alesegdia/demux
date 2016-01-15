package com.alesegdia.demux;

import com.alesegdia.demux.interp.Cubic;
import com.alesegdia.demux.interp.Interpolator;
import com.alesegdia.demux.interp.Lerper;
import com.alesegdia.demux.interp.Square;

public class Interpolators {

	static private Interpolators instance = null;
	
	public Interpolator speed = new Square(6f, 30f);
	public Interpolator ttl = new Cubic(0.2f, 1f);
	public Interpolator rate = new Square(0.01f, 1.f);
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
