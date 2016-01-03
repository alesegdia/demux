package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawingSystem extends EntitySystem {

	private SpriteBatch spriteBatch;

	public DrawingSystem( SpriteBatch spriteBatch ) {
		super(GraphicsComponent.class, TransformComponent.class);
		this.spriteBatch = spriteBatch;
	}
	
	@Override
	public void process(Entity e) {
		GraphicsComponent gc = (GraphicsComponent) e.getComponent(GraphicsComponent.class);
		TransformComponent pc = (TransformComponent) e.getComponent(TransformComponent.class);


		if( gc.allowFlip ) {
			if( gc.isFlipped && gc.flipX ) {
				gc.sprite.flip(true, false);
				gc.isFlipped = true;
			}
			
			if( !gc.isFlipped && !gc.flipX ) {
				gc.sprite.flip(true, false);
				gc.isFlipped = false;
			}
		}
		
		gc.sprite.setScale(GameConfig.PIXELS_TO_METERS);
		gc.sprite.setRotation(pc.angle);
		gc.sprite.setOriginCenter();
		gc.sprite.setAlpha(gc.alpha);
		gc.sprite.setPosition(pc.position.x - gc.sprite.getOriginX(), pc.position.y - gc.sprite.getOriginY());
		gc.sprite.draw(spriteBatch);
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
}
