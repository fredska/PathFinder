package com.fska.pf.map;

import com.badlogic.gdx.graphics.Color;

public enum TerrainType {
	
	MOUNTAIN_TOP(10, Color.WHITE),
	MOUNTAIN_SIDE(9, Color.LIGHT_GRAY),
	FOREST(6, Color.valueOf("00DE50FF")),
	FIELD(3, Color.valueOf("246632FF")),
	BEACH(4, Color.valueOf("FFE128FF")),
	TIDE_WATER(8, Color.valueOf("499DCFFF")),
	SEA_WATER(12, Color.valueOf("068AD8FF")),
	DEEP_SEA(15, Color.NAVY);
	
	private int pathCost;
	private Color color;
	TerrainType(int pathCost, Color color){
		this.pathCost = pathCost;
		this.color = color;
	}
	public int getPathCost() {
		return pathCost;
	}
	public Color getColor() {
		return color;
	}
}
