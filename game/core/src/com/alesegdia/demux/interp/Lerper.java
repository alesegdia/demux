package com.alesegdia.demux.interp;

public class Lerper extends Interpolator {

	public Lerper(float min, float max)
	{
		super(min, max);
	}

	@Override
	public float f(float p) {
		return p;
	}

}