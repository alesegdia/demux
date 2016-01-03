package com.alesegdia.demux.physics;

public class CollisionLayers {

	public static final short CATEGORY_MAP 			= 0x0001;
	public static final short GROUP_MAP 			= -1;
	
	public static final short CATEGORY_PLAYERPHYSIC	= 0x0002;
	public static final short GROUP_PLAYERPHYSIC	= -2;
	
	public static final short CATEGORY_PLAYERLOGIC	= 0x0004;
	public static final short GROUP_PLAYERLOGIC 	= -3;
	
	public static final short CATEGORY_PLBULLETS 	= 0x0008;
	public static final short GROUP_PLBULLETS 		= -4;
	
	public static final short CATEGORY_ENEMYPHYSIC 	= 0x0010;
	public static final short GROUP_ENEMYPHYSIC 	= -5;
	
	public static final short CATEGORY_ENEMYLOGIC 	= 0x0020;
	public static final short GROUP_ENEMYLOGIC 		= -6;
	
	public static final short CATEGORY_ENBULLETS 	= 0x0040;
	public static final short GROUP_ENBULLETS 		= -7;
	
	public static final short CATEGORY_1WAYPLATS 	= 0x0080;
	public static final short GROUP_1WAYPLATS 		= -8;
	
	public static final short CATEGORY_ENEMYLIMIT	= 0x0100;
	public static final short GROUP_ENEMYLIMIT 		= -9;
	
	public static final short CATEGORY_PICKUP		= 0x0200;
	public static final short GROUP_PICKUP 			= -10;

	public static final short CATEGORY_SHOP			= 0x0400;
	public static final short GROUP_SHOP 			= -11;

	public static final short CATEGORY_PORTAL		= 0x0800;
	public static final short GROUP_PORTAL 			= -12;

	/* MAP PHYSICS *************************************************************/
	public static final short MASK_MAP =
			CATEGORY_PLAYERPHYSIC 	| CATEGORY_PLBULLETS 	|
			CATEGORY_ENBULLETS 		| CATEGORY_ENEMYPHYSIC;
	public static final short MASK_1WAYPLATS = CATEGORY_PLAYERPHYSIC;
	public static final short MASK_ENEMYLIMIT = CATEGORY_ENEMYPHYSIC | CATEGORY_ENEMYLOGIC;

	/* PLAYER PHYSICS *************************************************************/
	public static final short MASK_PLAYERPHYSIC = CATEGORY_ENBULLETS | CATEGORY_1WAYPLATS | CATEGORY_MAP;
	public static final short MASK_PLAYERLOGIC = CATEGORY_ENEMYLOGIC | CATEGORY_ENBULLETS | CATEGORY_PICKUP | CATEGORY_SHOP | CATEGORY_PORTAL;
	public static final short MASK_PLBULLETS = CATEGORY_ENEMYLOGIC | CATEGORY_MAP | CATEGORY_SHOP;

	/* ENEMY PHYSICS *************************************************************/
	public static final short MASK_ENEMYPHYSIC = CATEGORY_MAP;
	public static final short MASK_ENEMYLOGIC = CATEGORY_PLAYERLOGIC | CATEGORY_PLBULLETS | CATEGORY_ENEMYLIMIT;
	public static final short MASK_ENBULLETS = CATEGORY_PLAYERLOGIC | CATEGORY_MAP;
	
	/* PICKUPS *******************************************************************/
	public static final short MASK_PICKUP = CATEGORY_PLAYERLOGIC;
	
	/* SHOPKEEPER ****************************************************************/
	public static final short MASK_SHOP = CATEGORY_PLAYERLOGIC | CATEGORY_PLBULLETS;
	
	public static final short MASK_PORTAL = CATEGORY_PLAYERLOGIC;
	

}
