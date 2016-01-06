package com.alesegdia.demux.components;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsComponent extends Component {

	public TextureRegion drawElement;
	public Sprite sprite = new Sprite();
	public boolean flipX = false;
	public boolean allowFlip = true;
	public float alpha = 1;
	public boolean isFlipped = false;
	public boolean hasShadowEffect = false;
	public float nextBufferCopy;
	
	public ShadowBufferEntry[] shadowBuffer = new ShadowBufferEntry[5];
	public int currentShadowIndex = 0;
	public boolean shadowEffectEnabled = false;
	
	public GraphicsComponent()
	{
		shadowBuffer = new ShadowBufferEntry[5];
		
		for( int i = 0; i < shadowBuffer.length; i++ )
		{
			shadowBuffer[i] = new ShadowBufferEntry(new Sprite(Gfx.playerDash.getKeyFrame(0).getTexture()));
		}
	}
	
}
