package com.alesegdia.demux.systems;

import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.badlogic.gdx.Gdx;

public class AttackTriggeringSystem extends EntitySystem {
	
	public AttackTriggeringSystem() {
		super(AttackComponent.class);
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Entity e) {
		
		AttackComponent ac = (AttackComponent) e.getComponent(AttackComponent.class);
		ac.nextAttackAvailable -= Gdx.graphics.getDeltaTime();

		if( ac.attackedLastFrame ) {
			// reset attack
			ac.isAttacking = true;
			ac.nextAttackAvailable = ac.attackCooldown;
			ac.attackedLastFrame = false;
			if( !ac.cooldownOverriden ) {
				ac.attackTimer = ac.attackDuration;
			}
		}
		
		if( ac.isAttacking ) {
			ac.attackTimer -= Gdx.graphics.getDeltaTime();
		}

		ac.isAttacking = ac.attackTimer >= 0;
		ac.canAttack = ac.nextAttackAvailable <= 0;
		ac.doAttack = ac.canAttack && ac.wantToAttack;

	}

}
