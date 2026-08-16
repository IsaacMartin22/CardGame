package io.github.starterproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TheGameClass extends Game {
    public AssetManager assets;
    public Stage stage;
    public Skin skin;

    public void create() {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());

        loadAssets();

        Table table = new Table();
        table.setFillParent(true);
        Label title = new Label("Card Game", skin);
        table.center();
        table.add(title);
        stage.addActor(table);

        // start with main menu screen
        this.setScreen(new MainMenuScreen(this));
    }

    private void loadAssets() {
        this.assets = new AssetManager();
        loadCards();
        loadSounds();
        loadBackgrounds();
        loadLogos();
        assets.finishLoading();
    }

    private void loadCards() {
        assets.load("cards/bone.png", Texture.class);
        assets.load("cards/leather.png", Texture.class);
        assets.load("cards/porkchop.png", Texture.class);
    }

    private void loadSounds() {
        assets.load("audio/sfx/drop.mp3", Sound.class);
        assets.load("audio/music/music.mp3", Music.class);
    }

    private void loadBackgrounds() {
        assets.load("backgrounds/background.png", Texture.class);
    }

    private void loadLogos() {
        assets.load("libgdx.png", Texture.class);
    }

    @Override
    public void render() {
        super.render(); // delegates to current screen
    }

    @Override
    public void dispose() {
        super.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (assets != null) assets.dispose();
    }

}
