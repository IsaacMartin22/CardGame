package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;

public class AncientScreen implements Screen {
    final TheGameClass game;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;

    private Stage stage;

    public AncientScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(game.assets.get("backgrounds/oasis.jpg", Texture.class));
        backgroundImage.setFillParent(true);

        stage.addActor(backgroundImage);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        debugOverlay.update("GameScreen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.screenStack.push(new DeckScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.screenStack.push(new MapScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
