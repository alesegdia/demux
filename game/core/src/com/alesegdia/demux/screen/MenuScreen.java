package com.alesegdia.demux.screen;

import java.util.List;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.WeaponStats;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.UpgradesComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen {

	private GdxGame g;

	public MenuScreen(GdxGame gdxGame) {
		this.g = gdxGame;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public static final int STATE_MAP = 0;
	public static final int STATE_SLOT = 1;
	public static final int STATE_REDIST = 2;
	
	int currentSlot = 0;
	
	int state = STATE_MAP;
	private int remainingPoints;
	private int selectedAttr;
	private WeaponStats editingWeaponStats;
	
	@Override
	public void render(float delta) {
		if( Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) )
		{
			if( state == STATE_MAP )
			{
				System.out.println("returning to gameplay screen");
				this.g.setScreen(g.tilemapScreen);
			}
			else if( state == STATE_SLOT )
			{
				state = STATE_MAP;
			}
			else if( state == STATE_REDIST )
			{
				// create weapon with new stats
				WeaponComponent wc = (WeaponComponent) g.tilemapScreen.gw.getPlayer().getComponent(WeaponComponent.class);
				AttackComponent atc = (AttackComponent) g.tilemapScreen.gw.getPlayer().getComponent(AttackComponent.class);
				ShootComponent sc = (ShootComponent) g.tilemapScreen.gw.getPlayer().getComponent(ShootComponent.class);
				
				// change weapon
				wc.weaponModel[this.currentSlot] = this.editingWeaponStats.makeModel();
				
				atc.attackCooldown = wc.weaponModel[this.currentSlot].rate;
				sc.bulletConfigs = wc.weaponModel[this.currentSlot].bulletEntries;
				
				state = STATE_SLOT;
			}
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		g.menuCam.update();
		g.batch.setProjectionMatrix(g.menuCam.combined);

		g.batch.begin();
		//Gfx.menuSprite.draw(g.batchMenu);
		g.batch.draw(Gfx.menuTexture, 0, 0);
		g.batch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		g.batch.setProjectionMatrix(g.menuCam.combined);
		g.batch.begin();
		g.batch.draw(Gfx.menuTexture, 0, 0);
		g.batch.end();

		handleInput();
		
		if( state == STATE_MAP )
		{
			stepMap();
		}
		else if( state == STATE_SLOT )
		{
			stepSlot();
		}
		else if( state == STATE_REDIST )
		{
			stepRedist();
		}

		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		UpgradesComponent uc = (UpgradesComponent) g.tilemapScreen.gw.getPlayer().getComponent(UpgradesComponent.class);
		g.batch.setProjectionMatrix(g.menuCam.combined);
		g.batch.begin();

		//uc.hasBinary = uc.hasTrivium = uc.hasWavegun = uc.hasDemux = uc.hasDash = uc.hasSJump = true;
		if( uc.hasBinary ) g.batch.draw(Gfx.pickupsSheet.get(8), 14, 66);
		if( uc.hasTrivium ) g.batch.draw(Gfx.pickupsSheet.get(3), 24, 66);
		if( uc.hasWavegun ) g.batch.draw(Gfx.pickupsSheet.get(4), 34, 66);
		if( uc.hasDemux ) g.batch.draw(Gfx.pickupsSheet.get(5), 44, 66);
		if( uc.hasDash ) g.batch.draw(Gfx.pickupsSheet.get(2), 24, 58);
		if( uc.hasSJump ) g.batch.draw(Gfx.pickupsSheet.get(6), 34, 58);
		
		g.batch.draw(Gfx.pickupsSheet.get(0), 14, 48);
		g.batch.draw(Gfx.pickupsSheet.get(7), 14, 38);
		g.batch.draw(Gfx.pickupsSheet.get(9), 14, 28);

		g.batch.end();
		
		g.batch.setProjectionMatrix(g.textCam.combined);
		g.batch.begin();
		StaminaComponent stc = (StaminaComponent) g.tilemapScreen.gw.getPlayer().getComponent(StaminaComponent.class);
		GaugeComponent gc = (GaugeComponent) g.tilemapScreen.gw.getPlayer().getComponent(GaugeComponent.class);
		g.font.draw(g.batch, "83/100", 120, 254);
		g.font.draw(g.batch, (stc.regenRate) + "/sec", 120, 206);
		g.font.draw(g.batch, ((int)gc.regenRate) + "/sec", 120, 158);
		g.batch.end();

	}
	
	
	
	private void stepRedist() {

		String powerStr = "power " + editingWeaponStats.power;
		if( this.selectedAttr == 0 ) powerStr += " < ";
		
		String ttlStr =   "ttl   " + editingWeaponStats.ttl;
		if( this.selectedAttr == 1 ) ttlStr += " < ";
		
		String speedStr = "speed " + editingWeaponStats.speed;
		if( this.selectedAttr == 2 ) speedStr += " < ";
		
		String rateStr =  "rate  " + editingWeaponStats.rate;
		if( this.selectedAttr == 3 ) rateStr += " < ";
		
		String remainingStr = "[" + this.remainingPoints + "] sp left";
		
		String str = powerStr + "\n" + ttlStr + "\n" + speedStr + "\n" + rateStr + "\n\n" + remainingStr + "\n\n<space> - assign point\n<escape> - save and quit";
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.S) )
		{
			this.selectedAttr = Math.min(selectedAttr + 1, 3);
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.W) )
		{
			this.selectedAttr = Math.max(selectedAttr - 1, 0);
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE) )
		{
			if( this.selectedAttr == 0 && this.editingWeaponStats.power + 1 <= this.editingWeaponStats.powerLimit )
			{
				int points_to_upgrade = this.editingWeaponStats.power;
				points_to_upgrade++;
				if( this.remainingPoints - points_to_upgrade >= 0 )
				{
					this.editingWeaponStats.power++;
					this.remainingPoints -= points_to_upgrade;
				}
			}

			if( this.selectedAttr == 1 && this.editingWeaponStats.ttl + 1 <= this.editingWeaponStats.ttlLimit )
			{
				int points_to_upgrade = this.editingWeaponStats.ttl;
				points_to_upgrade++;
				if( this.remainingPoints - points_to_upgrade >= 0 )
				{
					this.editingWeaponStats.ttl++;
					this.remainingPoints -= points_to_upgrade;
				}
			}

			if( this.selectedAttr == 2 && this.editingWeaponStats.speed + 1 <= this.editingWeaponStats.speedLimit )
			{
				int points_to_upgrade = this.editingWeaponStats.speed;
				points_to_upgrade++;
				if( this.remainingPoints - points_to_upgrade >= 0 )
				{
					this.editingWeaponStats.speed++;
					this.remainingPoints -= points_to_upgrade;
				}
			}

			if( this.selectedAttr == 3 && this.editingWeaponStats.rate + 1 <= this.editingWeaponStats.rateLimit )
			{
				int points_to_upgrade = this.editingWeaponStats.rate;
				points_to_upgrade++;
				if( this.remainingPoints - points_to_upgrade >= 0 )
				{
					this.editingWeaponStats.rate++;
					this.remainingPoints -= points_to_upgrade;
				}
			}

		}
		
		g.textCam.update();
		g.batch.setProjectionMatrix(g.textCam.combined);
		g.batch.begin();
		g.font.draw(g.batch, str, 320, 460);
		g.batch.end();
	}

	private void handleNum(int num_keycode, int slot )
	{
		if( Gdx.input.isKeyJustPressed(num_keycode) )
		{
			state = STATE_SLOT;
			currentSlot = slot;
		}
	}
	
	private void handleInput()
	{
		handleNum(Input.Keys.NUM_1, 0);
		handleNum(Input.Keys.NUM_2, 1);
		handleNum(Input.Keys.NUM_3, 2);
		handleNum(Input.Keys.NUM_4, 3);
	}
	
	private int getPlayerAP()
	{
		PlayerComponent pc = (PlayerComponent) g.tilemapScreen.gw.getPlayer().getComponent(PlayerComponent.class);
		return pc.accumulatedAbilityPoints;
	}

	private void stepSlot() {
		if( Gdx.input.isKeyJustPressed(Input.Keys.R) )
		{
			resetSkillsAndRedist();
		}

		WeaponComponent wc = (WeaponComponent) g.tilemapScreen.gw.getPlayer().getComponent(WeaponComponent.class);
		WeaponModel wm = wc.weaponModel[currentSlot];
		String str = "";
		str += "slot " + currentSlot + "\n======\n\n";

		String powerStr = "power ";
		String ttlStr = "ttl   ";
		String speedStr = "speed ";
		
		for( int i = 0; i < wm.bulletEntries.size(); i++ )
		{
			BulletEntry be = wm.bulletEntries.get(i);
			powerStr += (wm.ws.power);
			ttlStr += (wm.ws.ttl);
			speedStr += (wm.ws.speed);
			/*
			powerStr += norm(be.bm.power);
			ttlStr += norm(be.bm.destructionTime);
			speedStr += norm(be.bm.speed);
			*/
			if( i < wm.bulletEntries.size() - 1 )
			{
				powerStr += " + ";
				ttlStr += " + ";
				speedStr += " + ";
			}
		}
		
		str += powerStr + "\n";
		str += ttlStr + "\n";
		str += speedStr + "\n";
		//str += "rate  " + norm(wm.rate) + "\n";
		str += "rate  " + (wm.ws.rate) + "\n";
		
		str += "\n<R> - reset points";
		
		g.textCam.update();
		g.batch.setProjectionMatrix(g.textCam.combined);
		g.batch.begin();
		g.font.draw(g.batch, str, 320, 460);
		g.batch.end();
	}

	private void resetSkillsAndRedist() {
		state = STATE_REDIST;
		remainingPoints = getPlayerAP();
		selectedAttr = 0;
		editingWeaponStats = new WeaponStats(0, 0, 0, 0);
	}

	private int norm(float k) {
		return Math.round(k * 10);
	}

	private void stepMap() {
		g.srend.setAutoShapeType(true);
		g.srend.begin(ShapeType.Filled);

		Vector2 offset = computeMidPoint(g.restartScreen.roomLayout);
		
		for( Room r : g.restartScreen.roomLayout )
		{
			if( true && r.isVisited )
			{
				if( r == g.tilemapScreen.currentRoom )
				{
					g.srend.setColor(1,1,1,((float) Math.sin(Gdx.graphics.getFrameId()/10f) + 1f) / 2f );
				}
				else
				{
					if( r.rinfo.restriction.equals(new RestrictionSet(4, false, false, false, false)) )
					{
						g.srend.setColor(0.3f,0.3f,0.4f,1f);
					}
					else if( r.rinfo.restriction.equals(new RestrictionSet(4, true, false, false, false)) )
					{
						g.srend.setColor(0.2f,0.2f,0.4f,1f);
					}
					else
					{
						g.srend.setColor(0.5f,0.5f,0.5f,0.5f);
					}
				}
				float s = 12;
				g.srend.rect(500 + -offset.x * s + r.position.x * s, 200 + -offset.y * s + r.position.y * s, r.size.x * s, r.size.y * s);
			}
		}

		g.srend.end();
	}

	private Vector2 computeMidPoint(List<Room> roomLayout) {
		Vector2 ret = new Vector2(0,0);
		for( Room r : roomLayout )
		{
			ret.add(r.position.x, r.position.y);
		}
		ret.x /= roomLayout.size();
		ret.y /= roomLayout.size();
		return ret;
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
