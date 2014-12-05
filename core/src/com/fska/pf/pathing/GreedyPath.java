package com.fska.pf.pathing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;
import com.fska.pf.map.MapBase;
import com.fska.pf.map.TerrainType;

/**
 * Greedy Path Algorithm; Finds the shortest grid path from the start to end
 * nodes.
 * 
 * @author fskallos
 *
 */
public class GreedyPath extends PathFinderBase {

	/**
	 * If a collision is detected, try moving in the following order: N E S W
	 */
	@Override
	public void calculatePath(MapBase map, Vector2_Int startNode,
			Vector2_Int endNode) {
		if (nodePath == null) {
			nodePath = new ArrayList<Vector2_Int>();
		}

		Vector2_Int currentPosition = startNode;
		Vector2_Int nextPosition = startNode;
		nodePath.add(startNode);
		while (!currentPosition.equals(endNode)) {
			nodePath.add(getNextPosition(map, currentPosition, endNode).copy());
			currentPosition = nextPosition;
		}
	}

	private int moveUp = 1;
	private int moveRight = 1 << 2;
	private int moveLeft = 1 << 3;
	private int moveDown = 1 << 4;

	private Vector2_Int getNextPosition(MapBase map,
			Vector2_Int currentPosition, Vector2_Int endNode) {
		Vector2_Int nextPosition = currentPosition;
		int direction = 0;
		if (currentPosition.x < endNode.x) {
			nextPosition.x++;
			direction |= moveRight;
		}
		if (currentPosition.x > endNode.x) {
			nextPosition.x--;
			direction |= moveLeft;
		}
		if (currentPosition.y < endNode.y) {
			nextPosition.y++;
			direction |= moveDown;
		}
		if (currentPosition.y > endNode.y) {
			nextPosition.y--;
			direction |= moveUp;
		}
		/*
		System.out.println("Direction: " + direction);
		if (!ignoreWater) {
			if(map.getTerrainType(nextPosition.x, nextPosition.y).equals(
					TerrainType.TIDE_WATER)) {
				Set<Vector2_Int> nodeDirections = new HashSet<Vector2_Int>();
				for(int x = -1; x <= 1; x++){
					for(int y = -1; y <= 1; y++){
						if(x == 0 && y ==0)
							continue;
						nodeDirections.add(new Vector2_Int(currentPosition.getX() + x, currentPosition.getY() + y));
					}
				}
				
				while(map.getTerrainType(nextPosition.x, nextPosition.y).equals(
					TerrainType.TIDE_WATER)){
					
					nodeDirections.remove(nextPosition);
					if(nodeDirections.isEmpty())
						break;
					//Get the next best direction
					double distance = 999999999;
					Vector2 dblEndNode = new Vector2(endNode.getX(), endNode.getY());
					for(Vector2_Int node : nodeDirections){
						Vector2 dblNextNode = new Vector2(node.getX(), node.getY());
						if(dblNextNode.dst2(dblEndNode) < distance){
							distance = dblNextNode.dst2(dblEndNode);
							nextPosition = node.copy();
						}
					}
				}
			}
		}
		*/

		return nextPosition;
	}

	@Override
	public Pixmap drawPath(MapBase map) {
		if (nodePath == null) {
			calculatePath(map, new Vector2_Int(10, 10),
					new Vector2_Int(map.getWidth() - 20, map.getHeight() - 20));
		}
		Pixmap pixmap = new Pixmap(map.getWidth(), map.getHeight(),
				Format.RGBA8888);
		pixmap.setColor(Color.RED);

		for (Vector2_Int node : nodePath) {
			pixmap.drawPixel(node.x, node.y);
		}
		return pixmap;
	}

}
