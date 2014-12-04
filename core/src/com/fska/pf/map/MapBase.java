package com.fska.pf.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.fska.pf.noise.SimplexNoise;

public class MapBase {

	private double[][] tileMoveValue;
	private TerrainType[][] terrainData;

	private SimplexNoise sn;
	private int WIDTH, HEIGHT;

	public MapBase() {
		this(240, 180, 1234);
	}

	public MapBase(int width, int height, int seed) {
		this.WIDTH = width;
		this.HEIGHT = height;
		tileMoveValue = new double[WIDTH][HEIGHT];
		terrainData = new TerrainType[WIDTH][HEIGHT];
		sn = new SimplexNoise(275, 0.435, seed);

	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	private void generate() {
		generate(0);
	}

	private void generate(int depth) {
		for (int x = 0; x < tileMoveValue.length; x++) {
			for (int y = 0; y < tileMoveValue[0].length; y++) {
				// Simplex noise generates a value between -1 and 1
				double noiseValue = sn.getNoise(x, y, depth);
				tileMoveValue[x][y] = noiseValue;

				double[] thresholds = new double[] { 0.65, 0.5, 0.425, 0.075,
						-0.085, -0.2, -0.4, -0.6 };
				for (int c = 0; c < thresholds.length; c++) {
					if (noiseValue >= thresholds[c]) {
						terrainData[x][y] = TerrainType.values()[c];
						break;
					}
					terrainData[x][y] = TerrainType.DEEP_SEA;
				}

			}
		}
	}

	public Pixmap drawMap() {
		return drawMap(0);
	}

	public Pixmap drawMap(int depth) {
		generate(depth);
		Pixmap pixmap = new Pixmap(WIDTH, HEIGHT, Format.RGBA8888);
		for (int x = 0; x < tileMoveValue.length; x++) {
			for (int y = 0; y < tileMoveValue[0].length; y++) {
				pixmap.setColor(terrainData[x][y].getColor());
				pixmap.drawPixel(x, y);
			}
		}
		return pixmap;
	}

	public double getTileValue(int x, int y) {
		return tileMoveValue[x][y];
	}
	
	public TerrainType getTerrainType(int x, int y){
		return terrainData[x][y];
	}
}
