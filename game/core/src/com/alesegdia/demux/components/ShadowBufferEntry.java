package com.alesegdia.demux.components;

import com.alesegdia.demux.assets.Gfx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShadowBufferEntry {
	public Sprite sprite;
	public Vector2 position;
	public float alpha = 0;
	public boolean active = false;

	public ShadowBufferEntry( Sprite sprite )
	{
		this.sprite = sprite;
	}
}