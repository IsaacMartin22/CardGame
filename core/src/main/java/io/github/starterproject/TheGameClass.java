package io.github.starterproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TheGameClass extends Game {

    public Stage stage;
    public Skin skin;

    public void create() {
        // Use Scene2D UI for global/shared UI elements (if needed later)
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        Label title = new Label("Card Game", skin);
        table.center();
        table.add(title);
        stage.addActor(table);

        // start with main menu screen
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // delegates to current screen
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }

}
