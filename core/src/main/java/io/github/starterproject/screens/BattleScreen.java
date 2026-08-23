package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.actions.ScreenShakeAction;
import io.github.starterproject.actors.EnemyActor;
import io.github.starterproject.actors.HandActor;
import io.github.starterproject.actors.PlayerActor;
import io.github.starterproject.game.Battle;
import io.github.starterproject.game.BattleEnemy;
import io.github.starterproject.game.Hand;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.map.MapNodeType;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;
import java.util.concurrent.ThreadLocalRandom;

public class BattleScreen implements Screen {
    private static final float ATTACK_SOUND_VOLUME = 0.5f;
    private static final String[] REGULAR_ENEMY_BACKGROUNDS = {
        "backgrounds/autumn_orange_background.png",
        "backgrounds/autumn_red_background.png",
        "backgrounds/canyon_background.png",
        "backgrounds/colliseum_background.png",
        "backgrounds/desert_background.png",
        "backgrounds/dungeon_background.png",
        "backgrounds/forest_background.png",
        "backgrounds/plains_background.png",
        "backgrounds/snow_background.png"
    };

    final TheGameClass game;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;
    final Battle battle;

    private Stage stage;
    private HandActor handActor;
    private EnemyActor enemyActor;
    private PlayerActor playerActor;
    private Sound blockGainSound;
    private Sound attackSound;

    public BattleScreen(final TheGameClass game, MapNodeType type) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.battle = new Battle(game, new BattleEnemy());
        this.battle.initializePiles(game.deck);
        int initialDrawCount = this.battle.drawCards(Hand.HAND_LIMIT);

        String backgroundFilename = REGULAR_ENEMY_BACKGROUNDS[0];
        switch (type) {
            case BOSS:
                backgroundFilename = "backgrounds/colliseum_background.png";
                break;
            case ELITE:
                backgroundFilename = "backgrounds/desert_background.png";
                break;
            case ENEMY:
                backgroundFilename = getRandomEnemyBackground();
                break;
        }

        Image backgroundImage = new Image(game.assets.get(backgroundFilename, Texture.class));
        backgroundImage.setFillParent(true);


        this.blockGainSound = game.assets.get("audio/sfx/blacksmithhammer.mp3", Sound.class);
        this.attackSound = game.assets.get("audio/sfx/crash.ogg", Sound.class);

        enemyActor = new EnemyActor(game.skin, game.assets.get("nodes/enemy.png", Texture.class));
        enemyActor.setTouchable(Touchable.enabled);
        enemyActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (handActor.isSelectedCardRequiresTarget()) {
                    playSelectedCard();
                }
            }
        });

        playerActor = new PlayerActor(game.skin, game.assets.get("nodes/elite.png", Texture.class));

        TextButton endTurn = new TextButton("End Turn", game.skin);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add().expand().fill();
        rootTable.row();

        Table bottomBar = new Table();
        bottomBar.bottom().left();

        handActor = new HandActor(game.skin, game.assets);
        handActor.setPlayRequestHandler(this::playSelectedCard);
        handActor.setTargetHitTester(this::isPointerOverEnemy);
        bottomBar.add(handActor).expandX().fillX().height(220f).bottom().center();
        bottomBar.add(endTurn).width(200).height(100).pad(0f, 32f, 32f, 32f);

        rootTable.add(bottomBar).expandX().fillX().bottom();
        refreshHandView(initialDrawCount);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(enemyActor);
        stage.addActor(playerActor);
        stage.addActor(rootTable);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);

        endTurn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int damageTaken = battle.endPlayerTurn();
                if (damageTaken > 0) {
                    shakeScreen();
                }
                if (battle.getBattleResult() == Battle.BattleResult.DEFEAT) {
                    game.screenStack.push(new DeathScreen(game));
                    return;
                }
                if (battle.getBattleResult() == Battle.BattleResult.VICTORY) {
                    game.screenStack.push(new VictoryScreen(game));
                    return;
                }

                int drawnCards = battle.endEnemyTurn();
                refreshHandView(drawnCards);
            }
        });

        layoutBattlefield();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        playerActor.setStats(battle.getPlayerHealth(), battle.getPlayerMaxHealth(), battle.getPlayerBlock());
        enemyActor.setStats(battle.getEnemyHealth(), battle.getEnemyMaxHealth(), battle.getEnemyBlock());
        stage.act(delta);
        layoutBattlefield();
        debugOverlay.update("Battle Screen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.screenStack.push(new DeckScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.screenStack.push(new MapScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        layoutBattlefield();
        handActor.layoutHandCards();
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

    private void layoutBattlefield() {
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float actorY = worldHeight * 0.3f;
        float enemyX = worldWidth - enemyActor.getWidth() - worldWidth * 0.1f;
        enemyActor.setPosition(enemyX, actorY);

        float playerX = worldWidth * 0.1f;
        playerActor.setPosition(playerX, actorY);
    }

    private boolean playSelectedCard() {
        int selectedHandIndex = handActor.getSelectedHandIndex();
        if (selectedHandIndex < 0) {
            return false;
        }

        int blockBeforePlay = battle.getPlayerBlock();
        int enemyHealthBeforePlay = battle.getEnemyHealth();
        boolean played = battle.playCardFromHand(selectedHandIndex);
        if (!played) {
            handActor.layoutHandCards();
            return false;
        }
        if (battle.getEnemyHealth() < enemyHealthBeforePlay) {
            attackSound.play(ATTACK_SOUND_VOLUME);
        }
        if (battle.getPlayerBlock() > blockBeforePlay) {
            blockGainSound.play();
        }

        refreshHandView(0);

        if (battle.getBattleResult() == Battle.BattleResult.DEFEAT) {
            game.screenStack.push(new DeathScreen(game));
        }
        else if (battle.getBattleResult() == Battle.BattleResult.VICTORY) {
            game.screenStack.push(new VictoryScreen(game));
        }

        return true;
    }

    private boolean isPointerOverEnemy(float stageX, float stageY) {
        return enemyActor.hit(stageX - enemyActor.getX(), stageY - enemyActor.getY(), true) != null;
    }

    private void shakeScreen() {
        stage.getRoot().clearActions();
        stage.getRoot().addAction(new ScreenShakeAction(0.35f, 14f));
    }

    private void refreshHandView(int animatedCards) {
        handActor.setCards(battle.getHand().getCards(), animatedCards);
    }

    private String getRandomEnemyBackground() {
        int randomIndex = ThreadLocalRandom.current().nextInt(REGULAR_ENEMY_BACKGROUNDS.length);
        return REGULAR_ENEMY_BACKGROUNDS[randomIndex];
    }
}
