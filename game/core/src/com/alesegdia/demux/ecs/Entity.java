package com.alesegdia.demux.ecs;

import com.badlogic.gdx.utils.Bits;

public class Entity {

	public boolean isDead = false;
	private Bits bits = new Bits(32);
	public String name;
	Component[] components;
	
	public Entity() {
		components = new Component[64];
	}
	
	public Component addComponent(Component c1) {
		ComponentType ct = ComponentType.IsComponentRegistered(c1);
		if( ct == null ) {
			ct = ComponentType.RegisterComponentType(c1.getClass());
		}
		this.components[ComponentType.getIndexFor(c1.getClass())] = c1;
		this.bits.set(ct.getIndex());
		return c1;
	}

	public Bits getBits() {
		return bits;
	}
	
	public String toString() {
		return name;
	}

	public Component getComponent(Class<? extends Component> class1) {
		return this.components[ComponentType.getIndexFor(class1)];
	}


}
