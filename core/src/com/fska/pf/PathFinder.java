package com.fska.pf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fska.pf.map.MapBase;
import com.fska.pf.map.TerrainType;
import com.fska.pf.pathing.Dijkstra;
import com.fska.pf.pathing.GreedyPath;
import com.fska.pf.pathing.IPathFinder;
import com.fska.pf.pathing.Vector2_Int;

public class PathFinder extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture greedyPath;
	Texture dijkstraPath;
	private MapBase mb;
	private IPathFinder[] pathFinders;

	private Stage stage;

	Vector2_Int startNode, endNode;

	private Label startNodeCoord, endNodeCoord;
	private Label[] pathFinderCost;

	Skin defaultSkin;

	@Override
	public void create() {
		batch = new SpriteBatch();
		defaultSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		initialize();

		// Develop the side GUI to run different pathing algos;
		Viewport guiViewport = new ScreenViewport();
		stage = new Stage(guiViewport);
		Table baseTable = new Table(defaultSkin);

		Label textField_1 = new Label("Pathing Generator", defaultSkin);
		textField_1.setPosition(Gdx.graphics.getWidth() - 250,
				Gdx.graphics.getHeight() - 50);
		textField_1.setFontScale(1.5f);
		TextButton setStartNode = new TextButton("Set Start Location",
				defaultSkin);
		startNodeCoord = new Label(startNode.x + "," + startNode.y, defaultSkin);
		TextButton setEndNode = new TextButton("Set End Location", defaultSkin);
		endNodeCoord = new Label(endNode.x + "," + endNode.y, defaultSkin);
		TextButton showGreedyPathBtn = new TextButton("Greedy Path",
				defaultSkin);
		TextButton showDijkstrasPathBtn = new TextButton("Dijkstra's Path",
				defaultSkin);
		Label greedyTimeProcess = new Label("Time Greedy Pathing took (ms)",
				defaultSkin);
		TextButton resetButton = new TextButton("Reset", defaultSkin);

		baseTable.add(setStartNode);
		baseTable.add(startNodeCoord);
		baseTable.row();
		baseTable.add(setEndNode);
		baseTable.add(endNodeCoord);
		baseTable.row();
		baseTable.add(showGreedyPathBtn);
		baseTable.row();
		baseTable.add(showDijkstrasPathBtn);
		baseTable.row();
		baseTable.add(resetButton);
		baseTable.setPosition(Gdx.graphics.getWidth() - 150,
				Gdx.graphics.getHeight() - 150);
		greedyTimeProcess.setPosition(Gdx.graphics.getWidth() - 250,
				Gdx.graphics.getHeight() - 300);
		stage.addActor(greedyTimeProcess);
		stage.addActor(textField_1);
		stage.addActor(baseTable);

		// Add the cost labels to the stage;
		for (int c = 0; c < pathFinderCost.length; c++) {
			pathFinderCost[c].setPosition(Gdx.graphics.getWidth() - 200,
					Gdx.graphics.getHeight() - 325 - c * 25);
			stage.addActor(pathFinderCost[c]);
		}

		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		setStartNode.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ChangeEvent ce = (ChangeEvent) event;
					ce.handle();
					System.out.println("Click new Start Node...");
				}
				return false;
			}
		});

		setEndNode.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ChangeEvent ce = (ChangeEvent) event;
					ce.handle();
					System.out.println("Click new End Node...");
				}
				return false;
			}
		});

		showGreedyPathBtn.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ChangeEvent ce = (ChangeEvent) event;
					ce.handle();
					showGreedyPath = !showGreedyPath;
					System.out.println("Show / Hide the showGreedyPath");
				}
				return false;
			}
		});

		showDijkstrasPathBtn.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ChangeEvent ce = (ChangeEvent) event;
					ce.handle();
					showDijkstraPath = !showDijkstraPath;
					if (showDijkstraPath)
						System.out.println("Show the showDijkstraPath");
					else
						System.out.println("Hide the showDijkstraPath");
				}
				return false;
			}
		});

		resetButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ChangeEvent ce = (ChangeEvent) event;
					ce.handle();
					isResetState = true;
					System.out.println("Reset everything");
				}
				return false;
			}
		});
	}

	int depth = 0;

	private boolean isSetStartNode, isSetEndNode;
	private boolean isResetState;
	private boolean showGreedyPath;
	private boolean showDijkstraPath;

	@Override
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.draw(img, 0, 0, Gdx.graphics.getWidth() - 300,
				Gdx.graphics.getHeight());
		if (showGreedyPath)
			batch.draw(greedyPath, 0, 0, Gdx.graphics.getWidth() - 300,
					Gdx.graphics.getHeight());
		if(showDijkstraPath)
			batch.draw(dijkstraPath, 0, 0, Gdx.graphics.getWidth() - 300,
					Gdx.graphics.getHeight());
		batch.end();
		stage.draw();

		if (isResetState) {
			initialize();
			isResetState = false;
		}
	}

	private void initialize() {
		mb = new MapBase(160, 120, MathUtils.random(Integer.MAX_VALUE / 2));
		pathFinders = new IPathFinder[] { new GreedyPath(), new Dijkstra() };
		img = new Texture(mb.drawMap());
		// do{
		startNode = new Vector2_Int(MathUtils.random(mb.getWidth() / 2),
				MathUtils.random(mb.getHeight() / 2));
		// }
		// while(!mb.getTerrainType(startNode.getX(),
		// startNode.getY()).equals(TerrainType.FOREST));
		// do{
		endNode = new Vector2_Int(MathUtils.random(mb.getWidth() / 2)
				+ mb.getWidth() / 2 - 1, MathUtils.random(mb.getHeight() / 2)
				+ mb.getHeight() / 2 - 1);
		// }
		// while(!mb.getTerrainType(endNode.getX(),
		// endNode.getY()).equals(TerrainType.BEACH));
		System.out.println("Is start / end node the same? "
				+ startNode.equals(endNode));

		for (IPathFinder pathFinder : pathFinders) {
			pathFinder.calculatePath(mb, startNode, endNode);
		}

		greedyPath = new Texture(pathFinders[0].drawPath(mb));
		dijkstraPath = new Texture(pathFinders[1].drawPath(mb));
		if (startNodeCoord != null) {
			startNodeCoord.setText(startNode.x + "," + startNode.y);
			endNodeCoord.setText(endNode.getX() + "," + endNode.getY());
		}
		

		pathFinderCost = new Label[pathFinders.length];

		for (int c = 0; c < pathFinders.length; c++) {
			pathFinderCost[c] = new Label(pathFinders[c].getPathCost(mb)
					.toString(), defaultSkin);
		}
	}
}
