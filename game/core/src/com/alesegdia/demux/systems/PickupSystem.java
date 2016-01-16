package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.components.PickupItemComponent;

public class PickupSystem extends EntitySystem {

	public PickupSystem () {
		super(PickupEffectComponent.class, GaugeComponent.class);
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
			boolean wasPicked = false;
			
			switch( pic.pickupType ) {
			case HEAL:
				wasPicked = true;
				break;
			case INC_STAMINA:
				break;
			case TRI_MOD:
				break;
			case INC_SP:
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
