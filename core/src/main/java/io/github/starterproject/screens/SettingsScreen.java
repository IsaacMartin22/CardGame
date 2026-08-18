package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.TheGameClass;

public class SettingsScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;

    public SettingsScreen(final TheGameClass game) {
        this.game = game;

        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume", game.skin);
        TextButton saveAndQuit = new TextButton("Save and Quit", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        table.add(resumeButton).width(200).height(60).pad(10);
        table.row();
        table.add(saveAndQuit).width(200).height(60).pad(10);
        table.row();
        table.add(quitButton).width(200).height(60).pad(10);

        stage.addActor(table);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.pop();
                dispose();
            }
        });

        saveAndQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.popToRoot();
                dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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
