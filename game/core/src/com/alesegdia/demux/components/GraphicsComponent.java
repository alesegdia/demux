package com.alesegdia.demux.components;

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
	
}
