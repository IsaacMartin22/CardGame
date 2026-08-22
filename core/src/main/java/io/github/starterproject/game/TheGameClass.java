package io.github.starterproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.actors.CardActor;
import io.github.starterproject.screens.ScreenStack;
import io.github.starterproject.map.Level;
import io.github.starterproject.screens.MainMenuScreen;

public class TheGameClass extends Game {
    public AssetManager assets;
    public Stage stage;
    public Skin skin;
    public ScreenStack screenStack;

    public Deck deck;
    public Level level;
    public Player player;
    public RunInfo runInfo;
    public Difficulty difficulty;

    public void create() {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());
        this.deck = new Deck();
        this.screenStack = new ScreenStack(this);
        this.level = new Level();
        this.player = new Player();
        this.runInfo = new RunInfo();
        this.difficulty = new Difficulty();

        loadAssets();

        // start with main menu screen
        screenStack.push(new MainMenuScreen(this));
    }

    public void reset() {
        player.reset();
        deck.reset();
        level.reset();
        runInfo.reset();
    }

    private void loadAssets() {
        this.assets = new AssetManager();
        loadCards();
        loadSounds();
        loadMusic();
        loadBackgrounds();
        loadNodes();
        loadIcons();
        assets.finishLoading();
    }

    private void loadCards() {
        assets.load("cards/bone.png", Texture.class);
        assets.load("cards/leather.png", Texture.class);
        assets.load("cards/porkchop.png", Texture.class);
    }

    private void loadNodes() {
        assets.load("nodes/enemy.png", Texture.class);
        assets.load("nodes/elite.png", Texture.class);
        assets.load("nodes/event.png", Texture.class);
        assets.load("nodes/campfire.png", Texture.class);
        assets.load("nodes/merchant.png", Texture.class);
        assets.load("nodes/boss.png", Texture.class);
        assets.load("nodes/ancient.png", Texture.class);
    }

    private void loadIcons() {
        assets.load("icons/health.png", Texture.class);
        assets.load("icons/gold.png", Texture.class);
    }

    private void loadSounds() {
        assets.load("audio/sfx/blacksmithhammer.mp3", Sound.class);
        assets.load("audio/sfx/drop.mp3", Sound.class);
    }

    private void loadMusic() {
        assets.load("audio/music/music.mp3", Music.class);
        assets.load("audio/music/ice_music.mp3", Music.class);
    }

    private void loadBackgrounds() {
        // Jpg
        assets.load("backgrounds/arctic.jpg", Texture.class);
        assets.load("backgrounds/nighttime.jpg", Texture.class);
        assets.load("backgrounds/oasis.jpg", Texture.class);

        assets.load("backgrounds/battle_background.jpg", Texture.class);

        assets.load("backgrounds/death.jpg", Texture.class);

        // Png
        assets.load("backgrounds/snowscape.png", Texture.class);
        assets.load("backgrounds/map.png", Texture.class);
        assets.load("backgrounds/campfire_background.png", Texture.class);
        assets.load("backgrounds/merchant_background.png", Texture.class);
        assets.load("backgrounds/treasure_chest.png", Texture.class);
        assets.load("backgrounds/ancient_background.png", Texture.class);
        assets.load("backgrounds/boss_background.jpg", Texture.class);
        assets.load("backgrounds/elite_background.jpg", Texture.class);
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
        CardActor.disposeTemplates();
    }

}
