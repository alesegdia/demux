package com.alesegdia.demux.interp;

public abstract class Interpolator {

	private float max;
	private float min;

	public Interpolator(float min, float max)
	{
		this.min = min;
		this.max = max;
	}

	public float get( float p )
	{
		return this.min + ((this.max - this.min) * f(p));
	}

	public abstract float f(float p);

}
