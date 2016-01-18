package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameWorld;
import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.components.PickupItemComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.UpgradesComponent;
import com.alesegdia.demux.components.WeaponComponent;

public class PickupSystem extends EntitySystem {

	public PickupSystem () {
		super(PickupEffectComponent.class, GaugeComponent.class, UpgradesComponent.class);
	}
	
	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Entity e) {
		PickupEffectComponent pec = (PickupEffectComponent) e.getComponent(PickupEffectComponent.class);
		
		for( Entity pt : pec.pickupsCollectedLastFrame ) {
			PickupItemComponent pic = (PickupItemComponent) pt.getComponent(PickupItemComponent.class);
			UpgradesComponent uc = (UpgradesComponent) e.getComponent(UpgradesComponent.class);
			boolean wasPicked = false;
			
			switch( pic.pickupType ) {
			case HEAL:
				wasPicked = true;
				GameWorld.instance.notify("heal");
				pic.pickupEntry.collected = true;
				break;
			case INC_STAMINA:
				wasPicked = true;
				StaminaComponent sc = (StaminaComponent) GameWorld.instance.getPlayer().getComponent(StaminaComponent.class);
				sc.regenRate += 4;
				pic.pickupEntry.collected = true;
				GameWorld.instance.notify("stamina regen " + ((int)sc.regenRate));
				break;
			case TRI_MOD:
				wasPicked = true;
				GameWorld.instance.notify("trivium");
				pic.pickupEntry.collected = true;
				uc.hasTrivium = true;
				WeaponComponent wc = (WeaponComponent) e.getComponent(WeaponComponent.class);
				wc.weaponModel[1].ws.modifier = 2;
				updateWeapon(1, 2);
				break;
			case INC_SP:
				wasPicked = true;
				pic.pickupEntry.collected = true;
				PlayerComponent plc = (PlayerComponent) GameWorld.instance.getPlayer().getComponent(PlayerComponent.class);
				plc.accumulatedAbilityPoints += 18;
				GameWorld.instance.notify("sp " + plc.accumulatedAbilityPoints);
				break;
			case ABILITY_DASH:
				wasPicked = true;
				GameWorld.instance.notify("dash (press e/q for dashing)");
				pic.pickupEntry.collected = true;
				uc.hasDash = true;
				break;
			case ABILITY_SJUMP:
				wasPicked = true;
				GameWorld.instance.notify("dash jump");
				pic.pickupEntry.collected = true;
				uc.hasSJump = true;
				break;
			case BI_MOD:
				wasPicked = true;
				GameWorld.instance.notify("binary");
				pic.pickupEntry.collected = true;
				uc.hasBinary = true;
				WeaponComponent wec = (WeaponComponent) e.getComponent(WeaponComponent.class);
				wec.weaponModel[1].ws.modifier = 2;
				updateWeapon(0, 1);
				break;
			case DEMUX_MOD:
				wasPicked = true;
				GameWorld.instance.notify("demux");
				pic.pickupEntry.collected = true;
				uc.hasDemux = true;
				break;
			case INC_GAUGE:
				wasPicked = true;
				pic.pickupEntry.collected = true;
				GaugeComponent gc = (GaugeComponent) GameWorld.instance.getPlayer().getComponent(GaugeComponent.class);
				gc.regenRate += 4;
				GameWorld.instance.notify("gauge regen " + ((int)(gc.regenRate)) );
				break;
			case SINE_MOD:
				wasPicked = true;
				GameWorld.instance.notify("wavegun");
				pic.pickupEntry.collected = true;
				uc.hasWavegun = true;
				updateWeapon(2, 3);
				break;
			default:
				break;
			}
			
			pt.isDead = wasPicked;
		}
		if( !pec.pickupsCollectedLastFrame.isEmpty() )
		{
			pec.pickupsCollectedLastFrame.clear();
		}
	}

	public void updateWeapon(int slot, int modifier)
	{
		WeaponComponent wc = (WeaponComponent) GameWorld.instance.getPlayer().getComponent(WeaponComponent.class);
		AttackComponent atc = (AttackComponent) GameWorld.instance.getPlayer().getComponent(AttackComponent.class);
		ShootComponent sc = (ShootComponent) GameWorld.instance.getPlayer().getComponent(ShootComponent.class);
		
		// change weapon
		wc.weaponModel[slot].ws.modifier = modifier;
		wc.weaponModel[slot] = wc.weaponModel[slot].ws.makeModel();
		
		atc.attackCooldown = wc.weaponModel[slot].rate;
		sc.bulletConfigs = wc.weaponModel[slot].bulletEntries;
	}

}
