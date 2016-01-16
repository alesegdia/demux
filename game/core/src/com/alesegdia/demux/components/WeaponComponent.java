package com.alesegdia.demux.components;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.WeaponStats;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WeaponComponent extends Component {

	public static class WeaponModel {
		public WeaponModel(WeaponModel defaultGun) {
			this.rate = defaultGun.rate;
			bulletEntries = new LinkedList<BulletEntry>();
			for( BulletEntry be : defaultGun.bulletEntries )
			{
				bulletEntries.add(new BulletEntry(be));
			}
		}
		public WeaponModel() {

		}
		public float rate;
		public List<BulletEntry> bulletEntries;
		public WeaponStats ws;
	}
	
	public WeaponModel[] weaponModel = new WeaponModel[8];
	public int selectedWeapon = 0;

	public float computeShootCost() {
		WeaponModel wm = weaponModel[selectedWeapon];
		return wm.ws.computeShootCost();
	}
	
}
