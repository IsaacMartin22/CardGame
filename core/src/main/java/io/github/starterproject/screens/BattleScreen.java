package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
    private Group handLayer;
    private Image enemyImage;
    private CardActor selectedCardActor;
    private int selectedHandIndex = -1;
    private boolean handDirty = true;
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

        enemyImage = new Image(game.assets.get("nodes/enemy.png", Texture.class));
        enemyImage.setSize(180f, 180f);
        enemyImage.setTouchable(Touchable.enabled);
        enemyImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedCardActor != null && selectedCardActor.getCard().requiresTarget()) {
                    playSelectedCard();
                }
            }
        });

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
        handLayer = new Group();
        handLayer.setTouchable(Touchable.childrenOnly);
        rootTable.add(handLayer).expandX().fillX().height(220f).bottom().center().padBottom(2f);

        // background should be behind the UI table
        stage.addActor(backgroundImage);
        stage.addActor(enemyImage);
        stage.addActor(rootTable);

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);

        endTurn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                battle.endPlayerTurn();
                handDirty = true;
                if (battle.getBattleResult() == Battle.BattleResult.DEFEAT) {
                    game.screenStack.push(new DeathScreen(game));
                }
                else if (battle.getBattleResult() == Battle.BattleResult.VICTORY) {
                    game.screenStack.push(new VictoryScreen(game));
                }
            }
        });

        refreshHandView();
        layoutBattlefield();
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
        if (handDirty) {
            refreshHandView();
        }

        stage.act(delta);
        layoutBattlefield();
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
        layoutBattlefield();
        refreshHandView();
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

    private void refreshHandView() {
        handLayer.clearChildren();
        selectedCardActor = null;
        selectedHandIndex = -1;

        for (int i = 0; i < battle.getHand().getCards().size(); i++) {
            final int handIndex = i;
            final CardActor cardActor = new CardActor(battle.getHand().getCards().get(i), game.skin, game.assets);
            cardActor.setTouchable(Touchable.enabled);
            cardActor.addListener(new InputListener() {
                private float lastX;
                private float lastY;
                private boolean dragged;
                private boolean selectedBeforePress;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer != 0) {
                        return false;
                    }

                    selectedBeforePress = selectedCardActor == cardActor && selectedHandIndex == handIndex;
                    selectCard(handIndex, cardActor);
                    dragged = false;
                    lastX = x;
                    lastY = y;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (pointer != 0) {
                        return;
                    }

                    dragged = true;
                    cardActor.moveBy(x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer != 0) {
                        return;
                    }

                    if (cardActor.getCard().requiresTarget()) {
                        if (isPointerOverEnemy(event.getStageX(), event.getStageY())) {
                            playSelectedCard();
                        } else {
                            layoutHandCards();
                        }
                        return;
                    }

                    if (dragged && isDraggedFarEnough(cardActor)) {
                        playSelectedCard();
                        return;
                    }

                    if (selectedBeforePress) {
                        playSelectedCard();
                        return;
                    }

                    layoutHandCards();
                }
            });
            handLayer.addActor(cardActor);
        }

        layoutHandCards();
        handDirty = false;
    }

    private void layoutBattlefield() {
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float enemyX = (worldWidth - enemyImage.getWidth()) / 2f;
        float enemyY = worldHeight * 0.52f;
        enemyImage.setPosition(enemyX, enemyY);
    }

    private void layoutHandCards() {
        if (handLayer == null) {
            return;
        }

        float zoneWidth = stage.getViewport().getWorldWidth();
        float cardWidth = 100f;
        float cardHeight = 200f;
        float spacing = 14f;
        int cardCount = handLayer.getChildren().size;
        float totalWidth = cardCount * cardWidth + Math.max(0, cardCount - 1) * spacing;
        float startX = Math.max(12f, (zoneWidth - totalWidth) / 2f);
        float y = 10f;

        for (int i = 0; i < cardCount; i++) {
            CardActor cardActor = (CardActor) handLayer.getChildren().get(i);
            cardActor.setPosition(startX + i * (cardWidth + spacing), y);
            cardActor.setSize(cardWidth, cardHeight);
            cardActor.setSelected(cardActor == selectedCardActor);
        }

        if (selectedCardActor != null && selectedHandIndex >= 0 && selectedHandIndex < cardCount) {
            selectedCardActor.setSelected(true);
        }
    }

    private void selectCard(int handIndex, CardActor cardActor) {
        if (selectedCardActor != null && selectedCardActor != cardActor) {
            selectedCardActor.setSelected(false);
        }

        selectedCardActor = cardActor;
        selectedHandIndex = handIndex;
        cardActor.setSelected(true);
    }

    private boolean playSelectedCard() {
        if (selectedCardActor == null || selectedHandIndex < 0) {
            return false;
        }

        boolean played = battle.playCardFromHand(selectedHandIndex);
        if (!played) {
            layoutHandCards();
            return false;
        }

        selectedCardActor = null;
        selectedHandIndex = -1;
        handDirty = true;
        refreshHandView();

        if (battle.getBattleResult() == Battle.BattleResult.DEFEAT) {
            game.screenStack.push(new DeathScreen(game));
        }
        else if (battle.getBattleResult() == Battle.BattleResult.VICTORY) {
            game.screenStack.push(new VictoryScreen(game));
        }

        return true;
    }

    private boolean isDraggedFarEnough(CardActor cardActor) {
        return cardActor.getY() > 70f;
    }

    private boolean isPointerOverEnemy(float stageX, float stageY) {
        return enemyImage.hit(stageX, stageY, true) != null;
    }
}
