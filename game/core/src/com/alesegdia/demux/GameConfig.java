package com.alesegdia.demux;

public class GameConfig {

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	public static final float RATIO = ((float)WINDOW_WIDTH) / ((float)WINDOW_HEIGHT);
	public static final float VIEWPORT_WIDTH = 7f * 1.5f;
	public static final float VIEWPORT_HEIGHT = VIEWPORT_WIDTH / RATIO;
	
	public static final float METERS_TO_PIXELS = 16f;
	public static final float PIXELS_TO_METERS = 1f / METERS_TO_PIXELS;
	
	public static final float BLOCK_X = 12f;
	public static final float BLOCK_Y = 10f;
	
	
}
