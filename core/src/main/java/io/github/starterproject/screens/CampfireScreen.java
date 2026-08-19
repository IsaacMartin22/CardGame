package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;

public class CampfireScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;

    public CampfireScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Image campfire = new Image(game.assets.get("backgrounds/campfire_background.png", Texture.class));
        campfire.setFillParent(true);
//        ImageButton rest = new ImageButton(game.skin);
//        ImageButton upgrade = new ImageButton(game.skin);

        Table table = new Table();
        table.setFillParent(true);
        table.center().align(Align.center);
        table.add(campfire);
//        table.add(rest);
//        table.add(upgrade);

        stage.addActor(table);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CYAN);

        stage.act(delta);
        debugOverlay.update("CampfireScreen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.screenStack.pop();
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
        stage.dispose();
    }
}
