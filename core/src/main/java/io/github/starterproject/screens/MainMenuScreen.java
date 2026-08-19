package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;

public class MainMenuScreen implements Screen {
    private final TheGameClass game;
    private final Stage stage;

    public MainMenuScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(game.assets.get("backgrounds/snowscape.png", Texture.class));
        backgroundImage.setFillParent(true);

        Label titleLabel = new Label("SPIRE SLEIGHER", game.skin);
        titleLabel.setFontScale(5);

        Table table = new Table(game.skin);
        table.setFillParent(true);

        TextButton playButton = new TextButton("Play", game.skin);
        TextButton settingsButton = new TextButton("Settings", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        table.bottom().left();

        table.add(titleLabel).width(400).height(200).pad(200);
        table.row();
        table.row();
        table.row();
        table.row();
        table.row();
        table.row();
        table.add(playButton).width(200).height(60).pad(10);
        table.row();
        table.add(settingsButton).width(200).height(60).pad(10);
        table.row();
        table.add(quitButton).width(200).height(60).pad(10);

        stage.addActor(backgroundImage);
        stage.addActor(table);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MapScreen mapScreen = new MapScreen(game, true);
                game.screenStack.push(mapScreen);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openSettings();
                //dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    private void openSettings() {
        game.screenStack.push(new SettingsScreen(game));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();
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
