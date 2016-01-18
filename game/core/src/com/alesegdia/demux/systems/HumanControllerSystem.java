package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.AnimationComponent;
import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShadowBufferEntry;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.alesegdia.demux.components.UpgradesComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class HumanControllerSystem extends EntitySystem {

	public HumanControllerSystem() {
		super(PlayerComponent.class, PhysicsComponent.class,
			  LinearVelocityComponent.class, AnimationComponent.class,
			  GraphicsComponent.class, StaminaComponent.class);
	}
	
	@Override
	public void process(Entity e) {
		PhysicsComponent phc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
		LinearVelocityComponent lvc = (LinearVelocityComponent) e.getComponent(LinearVelocityComponent.class);
		AnimationComponent ac = (AnimationComponent) e.getComponent(AnimationComponent.class);
		PlayerComponent plc = (PlayerComponent) e.getComponent(PlayerComponent.class);
		GraphicsComponent gc = (GraphicsComponent) e.getComponent(GraphicsComponent.class);
		StaminaComponent stc = (StaminaComponent) e.getComponent(StaminaComponent.class);
		UpgradesComponent uc = (UpgradesComponent) e.getComponent(UpgradesComponent.class);
		
		// TODO: use 3 raycasts to check for grounded
		int dx = 0; int dy = 0;
		float prevYlinear = phc.body.getLinearVelocity().y;
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx = -1;
		} else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx = 1;
		}
		
		DashComponent dc = (DashComponent) e.getComponent(DashComponent.class);

		float DASH_COST = 10f;
		if( Gdx.input.isKeyJustPressed(Input.Keys.E) && stc.current - DASH_COST >= 0 && uc.hasDash ) 
		{
			dc.dashTimer = 0.3f;
			dc.dashIntensity = 6;
			stc.current -= DASH_COST;
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.Q) && stc.current - DASH_COST >= 0 && uc.hasDash ) 
		{
			dc.dashTimer = 0.3f;
			dc.dashIntensity = -6;
			stc.current -= DASH_COST;
		}
		
		if( !phc.grounded )
		{
			dc.dashTimer = 0;
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if( phc.grounded ) {
				plc.jumping = true;
				lvc.doCap[1] = false;
				float SUPERJUMP_COST = 10f;
				if( dc.dashTimer > 0 && stc.current - SUPERJUMP_COST >= 0 && uc.hasSJump )
				{
					prevYlinear = 8f;
					plc.superJump = true;
					stc.current -= SUPERJUMP_COST;
				}
				else
				{
					prevYlinear = 5.75f;
				}
			}
		}
		
		stc.current += stc.regenRate * Gdx.graphics.getDeltaTime();
		stc.current = Math.min(stc.max, stc.current + stc.regenRate * Gdx.graphics.getDeltaTime());
		
		if( plc.jumping ) {
			lvc.doCap[1] = false;
		}
		

		float vx = 5;
		if( plc.superJump )
		{
			vx = 5;
		}
		lvc.linearVelocity.x = dx * vx * lvc.speed.x;
		lvc.linearVelocity.y = prevYlinear;
		phc.body.setGravityScale(1);
		
		plc.isPressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
		
		if( phc.grounded ){
			if( Math.abs(phc.body.getLinearVelocity().x) > 0 ) {
				ac.currentAnim = Gfx.playerWalk;
			} else {
				ac.currentAnim = Gfx.playerStand;
			}
		} else {
			if( plc.jumping ) {
				if( phc.body.getLinearVelocity().y > 0.01 ) {
					ac.currentAnim = Gfx.playerJumpUp;
				} else {
					ac.currentAnim = Gfx.playerJumpDown;
				}
			}
		}
		
		if( dc.dashTimer > 0 )
		{
			ac.currentAnim = Gfx.playerDash;
			gc.shadowEffectEnabled = true;
		}
		else
		{
			gc.shadowEffectEnabled = false;
		}

		if( plc.superJump )
		{
			gc.shadowEffectEnabled = true;
		}
		
		AttackComponent atc = (AttackComponent) e.getComponent(AttackComponent.class);
		atc.wantToAttack = Gdx.input.isKeyJustPressed(Input.Keys.C) || Gdx.input.isKeyPressed(Input.Keys.V);
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.TAB) )
		{
			plc.showMap = !plc.showMap;
		}
		
		// change weapon
		ShootComponent sc = (ShootComponent) e.getComponent(ShootComponent.class);
		WeaponComponent wep = (WeaponComponent) e.getComponent(WeaponComponent.class);
		changeWeapon( Input.Keys.NUM_1, wep, 0, atc, sc );		
		changeWeapon( Input.Keys.NUM_2, wep, 1, atc, sc );		
		changeWeapon( Input.Keys.NUM_3, wep, 2, atc, sc );		
		changeWeapon( Input.Keys.NUM_4, wep, 3, atc, sc );
		
	}
	
	public void changeWeapon( int keycode, WeaponComponent wep, int i, AttackComponent atc, ShootComponent sc )
	{
		if( Gdx.input.isKeyJustPressed(keycode) )
		{
			atc.attackCooldown = wep.weaponModel[i].rate;
			sc.bulletConfigs = wep.weaponModel[i].bulletEntries;
			wep.selectedWeapon = i;
		}
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
	}
	
}
