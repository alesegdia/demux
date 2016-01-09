package com.alesegdia.demux.screen;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.map.MultipleConstraintComposer;
import com.alesegdia.demux.map.SingleConstraintComposer;
import com.alesegdia.troidgen.IWorldComposer;
import com.alesegdia.troidgen.renderer.RectDebugger;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.Screen;

public class RestartGameScreen implements Screen {

	private GdxGame g;
	List<Room> roomLayout;
	//private IWorldComposer composer = new SingleConstraintComposer();
	private IWorldComposer composer = new MultipleConstraintComposer();

	public RestartGameScreen(GdxGame gdxGame) {
		this.g = gdxGame;
	}

	@Override
	public void show() {

		List<Room> result = composer.compose(new LinkedList<Room>());
		this.roomLayout = result;
		
		Room r = result.get(0);
        g.tilemapScreen.reset(r);
        g.setScreen(g.tilemapScreen);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
