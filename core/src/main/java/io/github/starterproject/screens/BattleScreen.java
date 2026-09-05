package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.actions.CardFlickAction;
import io.github.starterproject.actions.ScreenShakeAction;
import io.github.starterproject.actors.CardActor;
import io.github.starterproject.actors.EnemyActor;
import io.github.starterproject.actors.HandActor;
import io.github.starterproject.actors.PlayerActor;
import io.github.starterproject.cards.Card;
import io.github.starterproject.cards.CardType;
import io.github.starterproject.cards.Defend;
import io.github.starterproject.cards.Shelter;
import io.github.starterproject.game.Battle;
import io.github.starterproject.game.BattleEnemy;
import io.github.starterproject.game.Hand;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.map.Enemy;
import io.github.starterproject.map.MapNodeType;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;
import java.util.concurrent.ThreadLocalRandom;

public class BattleScreen implements Screen {
    private static final float ATTACK_SOUND_VOLUME = 0.5f;
    private static final float PLAYED_CARD_FLICK_DURATION = 0.2f;
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

    private final Music battleMusic;
    private final Music eliteMusic;
    private final Music bossMusic;
    private Sound blockGainSound;
    private Sound attackSound;
    private MapNodeType type;

    public BattleScreen(final TheGameClass game, MapNodeType type) {
        this.game = game;
        this.type = type;
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

        enemyActor = new EnemyActor(game.skin, game.assets.get("characters/spider.png", Texture.class));
        enemyActor.setTouchable(Touchable.enabled);
        enemyActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (handActor.isSelectedCardRequiresTarget()) {
                    playSelectedCard();
                }
            }
        });

        playerActor = new PlayerActor(game.skin, game.assets.get("characters/shark.png", Texture.class));

        TextButton endTurn = new TextButton("End Turn", game.skin);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add().expand().fill();
        rootTable.row();

        handActor = new HandActor(game.skin, game.assets);
        handActor.setPlayRequestHandler(this::playSelectedCard);
        handActor.setTargetHitTester(this::isPointerOverEnemy);

        rootTable.row();
        rootTable.add(handActor).expandX().fillX().height(220f).bottom().center().padBottom(2f);

        Table endTurnTable = new Table();
        endTurnTable.setFillParent(true);
        endTurnTable.bottom().right();
        endTurnTable.add(endTurn).width(200).height(100).pad(0f, 32f, 32f, 32f);

        refreshHandView(initialDrawCount);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(enemyActor);
        stage.addActor(playerActor);
        stage.addActor(rootTable);
        stage.addActor(endTurnTable);

        this.debugOverlay = new DebugOverlay(stage, game.skin, game);
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

        this.battleMusic = game.assets.get("audio/music/battle_music.mp3", Music.class);
        battleMusic.setLooping(true);
        battleMusic.setVolume(.7f);

        this.eliteMusic = game.assets.get("audio/music/elite_music.mp3", Music.class);
        eliteMusic.setLooping(true);
        eliteMusic.setVolume(.7f);

        this.bossMusic = game.assets.get("audio/music/boss_music.mp3", Music.class);
        bossMusic.setLooping(true);
        bossMusic.setVolume(.7f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (type.equals(MapNodeType.BOSS)) {
            bossMusic.play();
        }
        else if (type.equals(MapNodeType.ELITE)) {
            eliteMusic.play();
        }
        else {
            battleMusic.play();
        }
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
            DeckScreen existingDeckScreen = game.screenStack.findTopmost(DeckScreen.class);
            if (existingDeckScreen != null) {
                game.screenStack.moveToTop(existingDeckScreen);
            }
            else {
                game.screenStack.push(new DeckScreen(game));
            }
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
    public void hide() {
        battleMusic.stop();
        eliteMusic.stop();
        bossMusic.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
        battleMusic.dispose();
        eliteMusic.dispose();
        bossMusic.dispose();
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
        Card selectedCard = handActor.getSelectedCard();
        Vector2 selectedCardCenter = new Vector2();
        boolean hasSelectedCardCenter = handActor.getSelectedCardStageCenter(selectedCardCenter);

        int blockBeforePlay = battle.getPlayerBlock();
        int enemyHealthBeforePlay = battle.getEnemyHealth();
        boolean played = battle.playCardFromHand(selectedHandIndex);
        if (!played) {
            handActor.layoutHandCards();
            return false;
        }
        boolean shouldPlayAttackSound = battle.getEnemyHealth() < enemyHealthBeforePlay;
        boolean shouldPlayBlockSound = battle.getPlayerBlock() > blockBeforePlay;
        Battle.BattleResult battleResult = battle.getBattleResult();
        if (selectedCard != null && hasSelectedCardCenter) {
            boolean startedFlickAnimation = playCardFlickAnimation(
                selectedCard,
                selectedCardCenter.x,
                selectedCardCenter.y,
                () -> finalizePlayedCard(shouldPlayAttackSound, shouldPlayBlockSound, battleResult)
            );
            if (!startedFlickAnimation) {
                finalizePlayedCard(shouldPlayAttackSound, shouldPlayBlockSound, battleResult);
            }
        }
        else {
            finalizePlayedCard(shouldPlayAttackSound, shouldPlayBlockSound, battleResult);
        }

        refreshHandView(0);

        return true;
    }

    private boolean isPointerOverEnemy(float stageX, float stageY) {
        return enemyActor.hit(stageX - enemyActor.getX(), stageY - enemyActor.getY(), true) != null;
    }

    private boolean playCardFlickAnimation(Card card, float startStageX, float startStageY, Runnable onComplete) {
        CardFlickAction.Target flickTarget = getFlickTarget(card);
        if (flickTarget == null) {
            return false;
        }

        Vector2 targetCenter = getFlickTargetCenter(flickTarget);
        CardActor flyCard = new CardActor(card, game.skin, game.assets);
        flyCard.setTouchable(Touchable.disabled);
        flyCard.setOrigin(flyCard.getWidth() / 2f, flyCard.getHeight() / 2f);
        flyCard.setPosition(startStageX - flyCard.getWidth() / 2f, startStageY - flyCard.getHeight() / 2f);
        stage.addActor(flyCard);
        flyCard.addAction(new CardFlickAction(
            flyCard.getX(),
            flyCard.getY(),
            targetCenter.x - flyCard.getWidth() / 2f,
            targetCenter.y - flyCard.getHeight() / 2f,
            PLAYED_CARD_FLICK_DURATION,
            flickTarget,
            onComplete
        ));
        return true;
    }

    private void finalizePlayedCard(boolean shouldPlayAttackSound, boolean shouldPlayBlockSound, Battle.BattleResult battleResult) {
        if (shouldPlayAttackSound) {
            attackSound.play(ATTACK_SOUND_VOLUME);
        }
        if (shouldPlayBlockSound) {
            blockGainSound.play();
        }

        if (battleResult == Battle.BattleResult.DEFEAT) {
            game.screenStack.push(new DeathScreen(game));
        }
        else if (battleResult == Battle.BattleResult.VICTORY) {
            game.screenStack.push(new VictoryScreen(game));
        }
    }

    private CardFlickAction.Target getFlickTarget(Card card) {
        if (card.getType() == CardType.ATTACK) {
            return CardFlickAction.Target.ENEMY;
        }
        if (card instanceof Defend || card instanceof Shelter) {
            return CardFlickAction.Target.PLAYER;
        }
        return null;
    }

    private Vector2 getFlickTargetCenter(CardFlickAction.Target target) {
        if (target == CardFlickAction.Target.ENEMY) {
            return getActorCenter(enemyActor);
        }
        return getActorCenter(playerActor);
    }

    private Vector2 getActorCenter(Actor actor) {
        return new Vector2(actor.getX() + actor.getWidth() / 2f, actor.getY() + actor.getHeight() / 2f);
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
