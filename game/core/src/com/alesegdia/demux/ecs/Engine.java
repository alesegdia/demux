package com.alesegdia.demux.ecs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Engine {

	List<EntitySystem> systems = new ArrayList<EntitySystem>();
	List<Entity> entities = new LinkedList<Entity>();
	private List<EntitySystem> renderingSystems = new ArrayList<EntitySystem>();
	private List<EntitySystem> stepSystems = new ArrayList<EntitySystem>();
	
	public Entity addEntity(Entity e) {
		entities.add(e);
		for( EntitySystem es : systems ) {
			es.entityAdded(e);
		}
		return e;
	}
	
	public void step() {
		for( EntitySystem es : stepSystems ) {
			es.process();
		}
		cleanupEntities();
	}
	
	public void render() {
		for( EntitySystem es : renderingSystems ) {
			es.process();
		}
	}
	
	void cleanupEntities() {
		List<Entity> toDel = new ArrayList<Entity>();
		for( Entity e : entities ) {
			if( e.isDead ) {
				toDel.add(e);
			}
		}
		
		for( Entity e : toDel ) {
			entities.remove(e);
			for( EntitySystem es : systems ) {
				es.entityRemoved(e);
			}
		}
	}
	
	public void addSystem(EntitySystem es) {
		addSystem(es, false);
	}
	
	public void addSystem(EntitySystem es, boolean rendering) {
		for( Entity e : entities ) {
			es.entityAdded(e);
		}
		if( rendering ) {
			renderingSystems.add(es);
		} else {
			stepSystems.add(es);
		}
		systems.add(es);
	}

	public int getNumEntities() {
		return entities.size();
	}
	
	public void Clear() {
		for( Entity e : this.entities ) {
			for( EntitySystem es : systems ) {
				es.entityRemoved(e);
			}
		}
		this.entities.clear();
	}
	
}
