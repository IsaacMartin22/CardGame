package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.TheGameClass;
import io.github.starterproject.cards.CardActor;

public class MapScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;
    private boolean locked;

    public MapScreen(final TheGameClass game) {
        this.game = game;
        this.locked = false;
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        table.top().center();

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
