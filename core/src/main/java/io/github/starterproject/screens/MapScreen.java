package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.TheGameClass;
import io.github.starterproject.map.MapNode;
import io.github.starterproject.map.MapNodeActor;

public class MapScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;
    private boolean locked;

    public MapScreen(final TheGameClass game) {
        this.game = game;
        this.locked = false;
        this.stage = new Stage(new ScreenViewport());

        Image mapBackground = new Image(game.assets.get("backgrounds/map.png", Texture.class));
        mapBackground.setAlign(Align.center);
        mapBackground.setPosition(game.stage.getWidth() / 2, game.stage.getHeight() / 2, Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.top().center();

        stage.addActor(mapBackground);

        for (MapNode node : game.floor.nodes) {
            table.add(new MapNodeActor(node, game.skin)).width(100).height(100).pad(10);
            table.row();
        }


        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GOLDENROD);

        stage.act(delta);
        //debugOverlay.update("GameScreen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (!locked) {
                game.screenStack.pop();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
