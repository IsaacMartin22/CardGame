package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    private final Music music;
    private final DistanceFieldFont titleFont;
    private final ShaderProgram titleShader;

    public MainMenuScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.titleFont = new DistanceFieldFont(Gdx.files.internal("fonts/spire-header.fnt"));
        this.titleFont.setDistanceFieldSmoothing(8f);
        this.titleShader = DistanceFieldFont.createDistanceFieldShader();
        this.stage.getBatch().setShader(titleShader);

        Image backgroundImage = new Image(game.assets.get("backgrounds/main_menu.png", Texture.class));
        backgroundImage.setFillParent(true);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.skin.get(Label.LabelStyle.class));
        titleStyle.font = titleFont;
        Label titleLabel = new Label("SPIRE SLEIGHER", titleStyle);
        titleLabel.setSize(1000, 200);
        titleLabel.setFontScale(1.20f);
        titleLabel.setColor(new Color(0.25f, 0.84f, 0.48f, 1f));

        Table table = new Table(game.skin);
        table.setFillParent(true);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(
            game.skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font = titleFont;
        buttonStyle.up = null;
        buttonStyle.down = null;
        buttonStyle.checked = null;
        buttonStyle.over = game.skin.newDrawable("white", new Color(1f, 1f, 1f, 0.25f));
        buttonStyle.disabled = null;

        TextButton playButton = new TextButton("Play", buttonStyle);
        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        TextButton quitButton = new TextButton("Quit", buttonStyle);

        table.bottom().left();

        table.add(titleLabel).width(1100).height(200).pad(200);
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
                game.screenStack.push(new MapScreen(game, true));
                music.stop();
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

        this.music = game.assets.get("audio/music/ice_music.mp3", Music.class);
        music.setLooping(true);
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
        music.play();
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
        titleFont.dispose();
        titleShader.dispose();
        music.dispose();
    }
}
