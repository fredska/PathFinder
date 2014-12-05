package com.fska.pf.pathing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.fska.pf.map.MapBase;

public class Dijkstra extends PathFinderBase {

	private int map_width, map_height;
	private Map<Vector2_Int, Vertex> graphVertex;
	private Vector2_Int end;

	@Override
	public Pixmap drawPath(MapBase map) {
		if (graphVertex == null) {
			calculatePath(map, new Vector2_Int(10, 10),
					new Vector2_Int(map.getWidth() - 20, map.getHeight() - 20));
		}

		Pixmap pixmap = new Pixmap(map.getWidth(), map.getHeight(),
				Format.RGBA8888);
		pixmap.setColor(Color.ORANGE);

		Vertex endPiece = getVertexNode(graphVertex, end);
		while (endPiece.getDist() != 0) {
			pixmap.drawPixel(endPiece.getX(), endPiece.getY());
			endPiece = endPiece.getPrev();
		}

		return pixmap;
	}

	@Override
	public void calculatePath(MapBase map, Vector2_Int start, Vector2_Int end) {
		this.end = end;
		map_width = map.getWidth();
		map_height = map.getHeight();
		graphVertex = new HashMap<Vector2_Int, Vertex>();
		Map<Vector2_Int, Vertex> Q = new HashMap<Vector2_Int, Vertex>();
		for (int x = 0; x < map_width; x++) {
			for (int y = 0; y < map_height; y++) {
				graphVertex.put(new Vector2_Int(x, y), new Vertex(x, y,
						Double.POSITIVE_INFINITY, null));
				Q.put(new Vector2_Int(x, y), new Vertex(x, y,
						Double.POSITIVE_INFINITY, null));
			}
		}
		// Set the source distance to 0;
		Q.get(start).setDist(0);

		Vertex u = null;
		while (!Q.isEmpty()) {
			// Start with the source vertex
			u = findMinDist(Q);
			Q.remove(u.getVector2_Int());

			// For each neighbor v of u;
			for (Vertex v : findNeighbors(Q, u)) {
				double alt = u.getDist() + map.getTileValue(v.x, v.y);
				if (alt < v.getDist()) {
					v.setDist(alt);
					v.setPrev(u);
					Vertex tmp = graphVertex.get(v.getVector2_Int());
					tmp.setPrev(graphVertex.get(u.getVector2_Int()));
					tmp.setDist(alt);
				}
			}
		}
	}

	private Vertex findMinDist(Map<Vector2_Int, Vertex> queue) {
		double min = Double.POSITIVE_INFINITY;
		Vertex result = queue.get(0);
		for (Vertex u : queue.values()) {
			if (u.getDist() < min) {
				min = u.getDist();
				result = u;
			}
		}
		return result;
	}

	private List<Vertex> findNeighbors(Map<Vector2_Int, Vertex> queue,
			Vertex source) {
		int sX = source.getX();
		int sY = source.getY();
		// Look only in up, down, left and right directions;
		Vector2_Int[] directions = new Vector2_Int[] {
				new Vector2_Int(sX, sY - 1), new Vector2_Int(sX, sY + 1),
				new Vector2_Int(sX - 1, sY), new Vector2_Int(sX + 1, sY) };

		List<Vertex> neighbors = new ArrayList<Vertex>();

		for (Vector2_Int direction : directions) {
			if (isValidCoordinate(direction.getX(), direction.getY())) {
				Vertex node = getVertexNode(queue, direction);
				if (node != null)
					neighbors.add(node);
			}
		}

		return neighbors;
	}

	// Returns true if coordinates fall within the
	// map boundaries
	private boolean isValidCoordinate(int x, int y) {
		return (x >= 0 && x < map_width) && (y >= 0 && y < map_height);
	}

	private Vertex getVertexNode(Map<Vector2_Int, Vertex> queue, Vector2_Int key) {
		return queue.get(key);
	}
}
