package com.alesegdia.demux.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spritesheet {
	
	private int tileWidth;
	private int tileHeight;
	private TextureRegion sheet;
	TextureRegion[] tiles;
	
	public Spritesheet( TextureRegion sheet, int xtiles, int ytiles )
	{
		this.sheet = sheet;
		loadSheet(xtiles, ytiles);
	}
	
	private void loadSheet(int xtiles, int ytiles) {
		tiles = new TextureRegion[xtiles * ytiles];
		
		tileWidth = this.sheet.getRegionWidth() / xtiles;
		tileHeight = this.sheet.getRegionHeight() / ytiles;		
		
		for( int i = 0; i < xtiles * ytiles; i++ )
		{
			int currentX = (int) ((i % xtiles) * tileWidth);
			int currentY = (int) ((i / xtiles) * tileHeight);
			
			TextureRegion reg = new TextureRegion(
					this.sheet,
					currentX, currentY,
					tileWidth, tileHeight);
			tiles[i] = reg;
		}
	}

	public Spritesheet(String path, int xtiles, int ytiles) {
		Texture pt = new Texture(Gdx.files.internal(path));
		TextureRegion tr = new TextureRegion();
		tr.setRegion(pt);
		this.sheet = tr;
		loadSheet(ytiles, xtiles);
	}

	public Sprite createSprite() {
		Sprite sprite = new Sprite( sheet.getTexture() );
		sprite.setSize( tileWidth,  tileHeight );
		return sprite;
	}
	
	public TextureRegion get( int p ) {
		return this.tiles[p];
	}
	
	public TextureRegion[] getRange( int from, int to ) {
		TextureRegion[] range = new TextureRegion[to - from + 1];
		for( int i = from; i <= to; i++ ) {
			range[i-from] = tiles[i];
		}
		return range;
	}
	
}
