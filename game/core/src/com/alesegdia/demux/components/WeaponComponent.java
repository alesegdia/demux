package com.alesegdia.demux.components;

import java.util.List;

import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WeaponComponent extends Component {

	public static enum WeaponType {
		DEFAULT, FIREBALL, SINEGUN, TRIPLASMA, SACREDGUN, TROQUEGUN
	}
	
	public static class WeaponModel {
		public float rate;
		public List<BulletEntry> bulletEntries;
		public TextureRegion tr;
	}
	
	public WeaponModel weaponModel;
	
}
