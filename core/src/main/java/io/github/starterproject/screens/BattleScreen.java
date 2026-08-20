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
import io.github.starterproject.cards.CardActor;
import io.github.starterproject.game.Battle;
import io.github.starterproject.game.BattleEnemy;
import io.github.starterproject.game.Hand;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.map.MapNodeType;
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
    private Table handTable;
    private Music music;

    public BattleScreen(final TheGameClass game, MapNodeType type) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.battle = new Battle(game, new BattleEnemy());
        this.battle.initializePiles(game.deck);
        this.battle.drawCards(Hand.HAND_LIMIT);

        String backgroundFilename = "backgrounds/oasis.jpg";
        switch (type) {
            case BOSS:
                backgroundFilename = "backgrounds/boss_background.jpg";
                break;
            case ELITE:
                backgroundFilename = "backgrounds/elite_background.jpg";
                break;
            case ENEMY:
                backgroundFilename = "backgrounds/oasis.jpg";
        }

        Image backgroundImage = new Image(game.assets.get(backgroundFilename, Texture.class));
        backgroundImage.setFillParent(true);

        this.music = game.assets.get("audio/music/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(.0f);

        handTable = new Table();
        handTable.left();

        TextButton endTurn = new TextButton("End Turn", game.skin);

        Table healthBars = new Table();
        healthBars.top().right();

        playerHealthLabel = new Label("Player Health: " + battle.getPlayerHealth(), game.skin);
        healthBars.add(playerHealthLabel).width(300).height(100).pad(100);

        enemyHealthLabel = new Label("Enemy Health: " + battle.getEnemyHealth(), game.skin);
        healthBars.add(enemyHealthLabel).width(300).height(100).pad(100);

        healthBars.add(endTurn).width(200).height(100).pad(100);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top();
        rootTable.row();
        rootTable.add().expandY().fill();
        rootTable.row();
        rootTable.add(healthBars).expandX().top().right();
        rootTable.row();
        rootTable.add(handTable).expandX().bottom().center().padBottom(2f);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(rootTable);

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
        refreshHandTable();

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

    private void refreshHandTable() {
        handTable.clearChildren();

        for (int i = 0; i < battle.getHand().getCards().size(); i++) {
            handTable.add(new CardActor(battle.getHand().getCards().get(i), game.skin, game.assets)).width(100).height(200).pad(8f);
        }
    }
}
