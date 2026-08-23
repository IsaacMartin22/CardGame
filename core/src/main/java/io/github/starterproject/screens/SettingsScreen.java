package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;

public class SettingsScreen implements Screen {
    private final TheGameClass game;
    private final Stage stage;
    private final Screen backgroundScreen;
    private final Texture dimTexture;

    public SettingsScreen(final TheGameClass game) {
        this.game = game;
        this.backgroundScreen = game.screenStack.peek();
        this.stage = new Stage(new ScreenViewport());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0.6f);
        pixmap.fill();
        this.dimTexture = new Texture(pixmap);
        pixmap.dispose();

        Image dimOverlay = new Image(dimTexture);
        dimOverlay.setFillParent(true);
        dimOverlay.setTouchable(Touchable.enabled);

        Table modal = new Table(game.skin);
        modal.setBackground("default-round");
        modal.defaults().width(220).height(60).pad(8);

        TextButton resumeButton = new TextButton("Resume", game.skin);
        TextButton credits = new TextButton("Credits", game.skin);
        TextButton saveAndQuit = new TextButton("Save and Quit", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        Label title = new Label("Settings", game.skin);
        modal.add(title).padBottom(12);
        modal.row();
        modal.add(resumeButton);
        modal.row();
        modal.add(credits);
        modal.row();
        modal.add(saveAndQuit);
        modal.row();
        modal.add(quitButton);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(modal).width(300).pad(20);

        stage.addActor(dimOverlay);
        stage.addActor(root);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.pop();
                dispose();
            }
        });

        credits.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.push(new CreditsScreen(game));
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
        if (backgroundScreen != null) {
            backgroundScreen.render(delta);
        }

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
        dimTexture.dispose();
    }
}
