package com.fska.pf.pathing;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;
import com.fska.pf.map.MapBase;

public class GreedyPath extends PathFinderBase {

	@Override
	public void calculatePath(MapBase map, Vector2_Int start, Vector2_Int end) {
		if(nodePath == null){
			nodePath = new ArrayList<Vector2_Int>();
		}
		
		Vector2_Int currentPosition = start;
		Vector2_Int nextPosition = start;
		nodePath.add(start);
		while(!currentPosition.equals(end)){
			if(currentPosition.x < end.x)
				nextPosition.x++;
			if(currentPosition.x > end.x)
				nextPosition.x--;
			
			if(currentPosition.y < end.y)
				nextPosition.y++;
			if(currentPosition.y > end.y)
				nextPosition.y--;
			
			nodePath.add(nextPosition.copy());
			currentPosition = nextPosition;
		}
	}

	@Override
	public Pixmap drawPath(MapBase map) {
		if(nodePath == null){
			calculatePath(map, new Vector2_Int(10, 10), new Vector2_Int(map.getWidth() - 20, map.getHeight() - 20));
		}
		Pixmap pixmap = new Pixmap(map.getWidth(), map.getHeight(), Format.RGBA8888);
		pixmap.setColor(Color.RED);
		
		for(Vector2_Int node : nodePath){
			pixmap.drawPixel(node.x, node.y);
		}
		return pixmap;
	}

}
