package com.alesegdia.demux.interp;

public class Cubic extends Interpolator {

	public Cubic(float min, float max) {
		super(min, max);
	}

	@Override
	public float f(float p) {
		return p * p * p;
	}

}
