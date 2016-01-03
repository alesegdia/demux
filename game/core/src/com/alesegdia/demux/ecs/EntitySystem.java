package com.alesegdia.demux.ecs;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Bits;

public abstract class EntitySystem implements IEntityListener {

	private Bits familyBits;
	private List<Entity> entities = new LinkedList<Entity>();
	public String name;
	
	public EntitySystem(Class<? extends Component>... componentClasses) {
		setComponentsFamily(componentClasses);
	}
	
	public void setComponentsFamily(Class<? extends Component>... componentClasses) {
		familyBits = new Bits(64);
		for( Class<? extends Component> c : componentClasses ) {
			familyBits.set(ComponentType.getIndexFor(c));
		}
	}

	boolean checkEntityBits( Entity e ) {
		Bits result = new Bits(familyBits.length());
		result.or(familyBits);
		result.and(e.getBits());
		return result.equals(familyBits);
	}
	
	@Override
	public void entityAdded(Entity e) {
		if( checkEntityBits(e) ) {
			//System.out.println("adding entity " + e + " from " + this);
			entities.add(e);
		}
	}

	@Override
	public void entityRemoved(Entity e) {
		if( checkEntityBits(e) ) {
			//System.out.println("removing entity " + e + " from " + this);
			entities.remove(e);
			notifyEntityRemoved(e);
		}
	}
	
	public abstract void notifyEntityRemoved(Entity e);
	
	public void process() {
		for( Entity e : entities ) {
			process(e);
		}
	}

	public abstract void process(Entity e);	
}
