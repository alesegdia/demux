package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.ShadowBufferEntry;
import com.alesegdia.demux.components.TransformComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
		if( gc.hasShadowEffect )
		{
			gc.nextBufferCopy -= Gdx.graphics.getDeltaTime();
			if( gc.nextBufferCopy <= 0 )
			{
				gc.nextBufferCopy = 0.1f;
				Sprite sprite = gc.shadowBuffer[gc.currentShadowIndex].sprite;
				sprite.setScale(GameConfig.PIXELS_TO_METERS);
				sprite.setRotation(pc.angle);
				sprite.setOriginCenter();
				sprite.setAlpha(gc.alpha);
				sprite.setPosition(pc.position.x - gc.sprite.getOriginX(), pc.position.y - gc.sprite.getOriginY());
				sprite.setTexture(gc.sprite.getTexture());
				sprite.setRegion(gc.sprite.getRegionX(), gc.sprite.getRegionY(), gc.sprite.getRegionWidth(), gc.sprite.getRegionHeight());
				
				gc.shadowBuffer[gc.currentShadowIndex].sprite = new Sprite(gc.sprite);
				gc.shadowBuffer[gc.currentShadowIndex].alpha = 0.5f;
				gc.shadowBuffer[gc.currentShadowIndex].active = gc.shadowEffectEnabled;

				gc.currentShadowIndex = (gc.currentShadowIndex + 1) % gc.shadowBuffer.length;				
			}
			
			for( ShadowBufferEntry sbe : gc.shadowBuffer )
			{
				if( sbe.active )
				{
					sbe.alpha -= Gdx.graphics.getDeltaTime()/2f;
					if( sbe.alpha < 0 ) sbe.alpha = 0;
					sbe.sprite.draw(spriteBatch, sbe.alpha);
				}
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
