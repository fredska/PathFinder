package com.fska.pf.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.fska.pf.noise.SimplexNoise;

public class MapBase {

	private double[][] tileMoveValue;

	private SimplexNoise sn;
	private int WIDTH, HEIGHT;

	public MapBase() {
		this(240, 180, 1234);
	}

	public MapBase(int width, int height, int seed) {
		this.WIDTH = width;
		this.HEIGHT = height;
		tileMoveValue = new double[WIDTH][HEIGHT];
		sn = new SimplexNoise(275, 0.435, seed);

	}
	
	public int getWidth(){return WIDTH;}
	public int getHeight(){return HEIGHT;}
	

	private void generate() {
		generate(0);
	}

	private void generate(int depth) {
		for (int x = 0; x < tileMoveValue.length; x++) {
			for (int y = 0; y < tileMoveValue[0].length; y++) {
				tileMoveValue[x][y] = sn.getNoise(x, y, depth);
			}
		}
	}

	public Pixmap drawMap() {
		return drawMap(0);
	}

	private double[] layerThresholds = null;
	private Color[] layerColors = null;

	public Pixmap drawMap(int depth) {
		if (layerThresholds == null) {
//			layerThresholds = new double[] { .225, 0.185, 0.115,0.025 };
//			layerColors = new Color[] { Color.GREEN, Color.DARK_GRAY,
//					Color.GRAY, Color.LIGHT_GRAY};
			
			layerThresholds = new double[] { 0.635, 0.435, 0.315,0.0,-0.125, -0.65 };
			layerColors = new Color[] { Color.WHITE, Color.LIGHT_GRAY,
					Color.GRAY, Color.DARK_GRAY, new Color(0x006F48FF), Color.BLUE};
		}
		generate(depth);
		Pixmap pixmap = new Pixmap(WIDTH, HEIGHT, Format.RGBA8888);
		for (int x = 0; x < tileMoveValue.length; x++) {
			for (int y = 0; y < tileMoveValue[0].length; y++) {
				for (int c = 0; c < layerThresholds.length; c++) {
					if (tileMoveValue[x][y] >= layerThresholds[c]) {
						pixmap.setColor(layerColors[c]);
						pixmap.drawPixel(x, y);
						break;
					}
				}
			}
		}
		return pixmap;
	}
	
	public double getTileValue(int x, int y){
		return tileMoveValue[x][y];
	}
}
