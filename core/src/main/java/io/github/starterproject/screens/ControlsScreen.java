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

public class ControlsScreen implements Screen {
    private final TheGameClass game;
    private final Stage stage;
    private final Screen backgroundScreen;
    private final Texture dimTexture;

    public ControlsScreen(final TheGameClass game) {
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
        modal.defaults().width(300).pad(8);

        Label title = new Label("Controls", game.skin);
        modal.add(title).padBottom(12);
        modal.row();

        addControlLine(modal, "ESC", "Open Settings / Go Back");
        addControlLine(modal, "D", "Open Deck Screen");
        addControlLine(modal, "M", "Go to Map Screen");
        addControlLine(modal, "Arrows/WASD", "Navigate");

        TextButton backButton = new TextButton("Back", game.skin);
        backButton.setWidth(300);
        backButton.setHeight(60);
        modal.row();
        modal.add(backButton).width(300).height(60).padTop(12);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(modal).width(400).pad(20);

        stage.addActor(dimOverlay);
        stage.addActor(root);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.pop();
                dispose();
            }
        });
    }

    private void addControlLine(Table table, String key, String description) {
        Table controlRow = new Table(game.skin);
        controlRow.defaults().width(175).height(40).pad(4);

        Label keyLabel = new Label(key, game.skin);
        Label descLabel = new Label(description, game.skin);

        controlRow.add(keyLabel).width(150);
        controlRow.add(descLabel).width(200);

        table.add(controlRow).width(350);
        table.row();
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
