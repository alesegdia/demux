package com.alesegdia.demux.interp;

public class Square extends Interpolator {

	public Square(float min, float max) {
		super(min, max);
	}

	@Override
	public float f(float p) {
		return p * p;
	}

}
