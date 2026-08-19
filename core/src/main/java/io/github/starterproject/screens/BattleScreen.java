package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.Battle;
import io.github.starterproject.game.BattleEnemy;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;

public class BattleScreen implements Screen {
    final TheGameClass game;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;
    final Battle battle;
    public Label playerHealthLabel;
    public Label enemyHealthLabel;

    private Stage stage;
    private Music music;

    public BattleScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.battle = new Battle(game, new BattleEnemy());

        Image backgroundImage = new Image(game.assets.get("backgrounds/oasis.jpg", Texture.class));
        backgroundImage.setFillParent(true);

        this.music = game.assets.get("audio/music/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(.0f);

        TextButton endTurn = new TextButton("End Turn", game.skin);

        Table table = new Table();
        table.setFillParent(true);
        table.bottom().right();

        playerHealthLabel = new Label("Player Health: " + battle.getPlayerHealth(), game.skin);
        table.add(playerHealthLabel).width(300).height(100).pad(100);

        enemyHealthLabel = new Label("Enemy Health: " + battle.getEnemyHealth(), game.skin);
        table.add(enemyHealthLabel).width(300).height(100).pad(100);

        table.add(endTurn).width(200).height(100).pad(100);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(table);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);

        endTurn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                battle.endPlayerTurn();
                if (battle.getBattleResult() == Battle.BattleResult.DEFEAT) {
                    game.screenStack.push(new DeathScreen(game));
                }
                else if (battle.getBattleResult() == Battle.BattleResult.VICTORY) {
                    game.screenStack.push(new VictoryScreen(game));
                }
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
        ScreenUtils.clear(Color.BLACK);

        playerHealthLabel.setText("Player Health: " + battle.getPlayerHealth());
        enemyHealthLabel.setText("Enemy Health: " + battle.getEnemyHealth());

        stage.act(delta);
        debugOverlay.update("GameScreen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.screenStack.push(new DeckScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.screenStack.push(new MapScreen(game));
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
