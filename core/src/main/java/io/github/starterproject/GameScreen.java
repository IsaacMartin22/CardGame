package io.github.starterproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
    final TheGameClass game;

    private Stage stage;
    private Skin skin;

    private Texture background;
    private Texture image;
    private Image backgroundImage;
    private Image imageActor;
    private Label fpsLabel;

    private Sound dropSound;
    private Music music;

    public GameScreen(final TheGameClass game) {
        this.game = game;

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());

        background = new Texture("background.png");
        image = new Texture("libgdx.png");

        backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);

        imageActor = new Image(image);

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);

        Table table = new Table();
        table.setFillParent(true);
        table.top().left();

        fpsLabel = new Label("FPS: 0", skin);

        table.add(imageActor).expand().center();
        table.row();
        table.add(fpsLabel).left().pad(10);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(table);

        // clicking anywhere returns to main menu (keeps previous behavior)
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        music.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        stage.act(delta);
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new SettingsScreen(game, this));
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
        skin.dispose();
        image.dispose();
        background.dispose();
        dropSound.dispose();
        music.dispose();
    }
}
