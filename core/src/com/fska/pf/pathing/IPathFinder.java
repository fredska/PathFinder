package com.fska.pf.pathing;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.fska.pf.map.MapBase;


public interface IPathFinder {
	public Long getPathCost(MapBase map);
	public void calculatePath(MapBase map, Vector2_Int start, Vector2_Int end);
	
	public Pixmap drawPath(MapBase map);
	public void ignoreWater(boolean ignoreWater);
}
