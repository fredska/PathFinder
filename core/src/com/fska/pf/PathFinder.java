package com.fska.pf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fska.pf.map.MapBase;
import com.fska.pf.pathing.GreedyPath;
import com.fska.pf.pathing.IPathFinder;
import com.fska.pf.pathing.Vector2_Int;

public class PathFinder extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture path;

	private MapBase mb;
	private IPathFinder[] pathFinders;

	private Stage stage;

	@Override
	public void create() {
		batch = new SpriteBatch();
		mb = new MapBase(640, 480, MathUtils.random(Integer.MAX_VALUE / 2));

		img = new Texture(mb.drawMap());

		pathFinders = new IPathFinder[] { new GreedyPath() };

		pathFinders[0].calculatePath(
				mb,
				new Vector2_Int(MathUtils.random(mb.getWidth() / 2), MathUtils
						.random(mb.getHeight() / 2)),
				new Vector2_Int(MathUtils.random(mb.getWidth() / 2)
						+ mb.getWidth() / 2,
						MathUtils.random(mb.getHeight() / 2) + mb.getHeight()
								/ 2));

		path = new Texture(pathFinders[0].drawPath(mb));

		// Develop the side GUI to run different pathing algos;
		Viewport guiViewport = new ScreenViewport();
		guiViewport.update(100, 100);
		stage = new Stage(guiViewport);
		Skin defaultSkin = new Skin(
				Gdx.files.internal("data/uiskin.json"));
		Table baseTable = new Table(defaultSkin);
		
		Label textField_1 = new Label("Pathing Generator", defaultSkin);
		textField_1.setPosition(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 50);
		textField_1.setFontScale(1.5f);
		TextButton setStartNode = new TextButton("Set Start Location", defaultSkin);
		TextButton setEndNode = new TextButton("Set End Location",defaultSkin);
		TextButton showGreedyPath = new TextButton("Greedy Path", defaultSkin);
		Label greedyTimeProcess = new Label("Time Greedy Pathing took (ms)",defaultSkin);
		TextButton resetButton = new TextButton("Reset", defaultSkin);
		baseTable.add(setStartNode);
		baseTable.row();
		baseTable.add(setEndNode);
		baseTable.row();
		baseTable.add(showGreedyPath);
		baseTable.add(greedyTimeProcess);
		baseTable.row();
		baseTable.add(resetButton);
		baseTable.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 100);
		stage.addActor(textField_1);
		stage.addActor(baseTable);
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);
	}

	int depth = 0;

	@Override
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		batch.draw(img, 0, 0, Gdx.graphics.getWidth() - 300,
				Gdx.graphics.getHeight());
		batch.draw(path, 0, 0, Gdx.graphics.getWidth() - 300,
				Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
		// depth++;
		// img = new Texture(mb.drawMap(depth));
	}
}
