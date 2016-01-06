package com.alesegdia.demux;

import com.alesegdia.troidgen.room.Direction;
import com.badlogic.gdx.math.Vector2;

public class DirectionUtils {
	public static Vector2 GetOffsetForDirection(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = GameConfig.BLOCK_X / 2f;
			offset.y = GameConfig.BLOCK_Y - 0.5f;
			break;
		case DOWN:
			offset.x = GameConfig.BLOCK_X / 2f;
			offset.y = 0.5f;
			break;			
		case LEFT:
			offset.x = 0.5f;
			offset.y = 2f;
			break;
		case RIGHT:
			offset.x = GameConfig.BLOCK_X - 0.5f;
			offset.y = 2f;
		}
		return offset;
	}

	public static Vector2 GetSizeForDirection(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = 0.25f;
			offset.y = 0.25f;
			break;
		case DOWN:
			offset.x = 0.50f;
			offset.y = 0.255f;
			break;			
		case LEFT:
			offset.x = 0.25f;
			offset.y = 0.25f;
			break;
		case RIGHT:
			offset.x = 0.25f;
			offset.y = 0.25f;
		}
		return offset;
	}

	public static int GetIndexForDirection(Direction dir) {
		switch(dir)
		{
		case TOP: return 2;
		case DOWN: return 3;
		case LEFT: return 1;
		case RIGHT: return 0;
		}
		return -1;
	}


}
