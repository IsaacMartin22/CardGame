package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class CreditsScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;

    public CreditsScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);

        Label credits = new Label("Credits", game.skin);
        String creditsText = "Game developed by Isaac Martin\n";
        creditsText += "Music: Icy Realm (7 and 8) by The Cynic Project / pixelsphere.org / cynicmusic.com\n";

        credits.setText(creditsText);
        table.add(credits).pad(10);
        table.row();

        TextButton returnToTitle = new TextButton("Return to Title Screen", game.skin);

        table.add(returnToTitle).width(200).height(60).pad(10);

        stage.addActor(table);

        returnToTitle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.popToRoot();
                game.reset();
                dispose();
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
