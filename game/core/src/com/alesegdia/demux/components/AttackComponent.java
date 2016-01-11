package com.alesegdia.demux.components;

import com.alesegdia.demux.ecs.Component;

public class AttackComponent extends Component {

	public boolean canAttack = false;
	public float attackCooldown; 			// time between attacks
	public float nextAttackAvailable; 		// time to be able to attack
	public boolean attackedLastFrame; 		// did attack last frame?
	public boolean wantToAttack = false;	// will to attack
	public boolean doAttack; 				// actually can perform an attack

	public boolean isAttacking = false;
	public float attackDuration;
	public float attackTimer;
	public boolean cooldownOverriden;
	public int forceFace;
}
