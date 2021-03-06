package com.fska.pf.pathing;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.fska.pf.map.MapBase;

public abstract class PathFinderBase implements IPathFinder {

	protected List<Vector2_Int> nodePath;
	protected boolean ignoreWater;
	@Override
	public Long getPathCost(MapBase map) {
		if(nodePath == null)
			return -1l;
		double result = 0;
		
		for(Vector2_Int node : nodePath){
			result += map.getTerrainType(node.x, node.y).getPathCost();
		}
		
		return (long)result;
	}

	public abstract void calculatePath(MapBase map, Vector2_Int start, Vector2_Int end);
	
	public void ignoreWater(boolean ignoreWater){
		this.ignoreWater = ignoreWater;
	}
}
