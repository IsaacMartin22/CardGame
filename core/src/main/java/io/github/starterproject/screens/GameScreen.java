package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.TheGameClass;

public class GameScreen implements Screen {
    final TheGameClass game;
    final DebugOverlay debugOverlay;

    private Stage stage;

    private Sound dropSound;
    private Music music;

    public GameScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(game.assets.get("backgrounds/background.png", Texture.class));
        backgroundImage.setFillParent(true);

        Image imageActor = new Image(game.assets.get("libgdx.png", Texture.class));

        this.dropSound = game.assets.get("audio/sfx/drop.mp3", Sound.class);
        this.music = game.assets.get("audio/music/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(.0f);

        Table table = new Table();
        table.setFillParent(true);
        table.top().left();

        table.add(imageActor).expand().center();

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(table);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        music.play();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
            music.pause();
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
