package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameWorld;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.components.PickupItemComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.UpgradesComponent;

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
				GameWorld.instance.notify("+stamina");
				StaminaComponent sc = (StaminaComponent) GameWorld.instance.getPlayer().getComponent(StaminaComponent.class);
				sc.regenRate += 1;
				pic.pickupEntry.collected = true;
				break;
			case TRI_MOD:
				wasPicked = true;
				GameWorld.instance.notify("trivium");
				pic.pickupEntry.collected = true;
				uc.hasTrivium = true;
				break;
			case INC_SP:
				wasPicked = true;
				GameWorld.instance.notify("+sp");
				pic.pickupEntry.collected = true;
				break;
			case ABILITY_DASH:
				wasPicked = true;
				GameWorld.instance.notify("dash");
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
				break;
			case DEMUX_MOD:
				wasPicked = true;
				GameWorld.instance.notify("demux");
				pic.pickupEntry.collected = true;
				uc.hasDemux = true;
				break;
			case INC_GAUGE:
				wasPicked = true;
				GameWorld.instance.notify("+gauge");
				pic.pickupEntry.collected = true;
				GaugeComponent gc = (GaugeComponent) GameWorld.instance.getPlayer().getComponent(GaugeComponent.class);
				gc.regenRate += 1;
				break;
			case SINE_MOD:
				wasPicked = true;
				GameWorld.instance.notify("wavegun");
				pic.pickupEntry.collected = true;
				uc.hasWavegun = true;
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

}
