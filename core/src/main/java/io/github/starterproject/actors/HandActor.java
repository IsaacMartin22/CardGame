package io.github.starterproject.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.starterproject.Constants;
import io.github.starterproject.actions.CardDrawAction;
import io.github.starterproject.cards.Card;

import java.util.List;

public class HandActor extends Group {
    public interface PlayRequestHandler {
        boolean playSelectedCard();
    }

    public interface TargetHitTester {
        boolean isTargetHit(float stageX, float stageY);
    }

    private static final float BASE_Y = 10f;
    private static final float SELECTED_RAISE = 28f;
    private static final float CENTER_RAISE = 8f;
    private static final float MAX_ROTATION = 6f;
    private static final float DRAW_START_OFFSET_Y = 120f;
    private static final float DRAW_DURATION = 0.45f;

    private final Skin skin;
    private final AssetManager assets;

    private PlayRequestHandler playRequestHandler;
    private TargetHitTester targetHitTester;
    private CardActor selectedCardActor;
    private CardActor draggingCardActor;
    private boolean draggingCardReadyToPlay;
    private int selectedHandIndex = -1;

    public HandActor(Skin skin, AssetManager assets) {
        this.skin = skin;
        this.assets = assets;
        setTouchable(Touchable.childrenOnly);
    }

    public void setPlayRequestHandler(PlayRequestHandler playRequestHandler) {
        this.playRequestHandler = playRequestHandler;
    }

    public void setTargetHitTester(TargetHitTester targetHitTester) {
        this.targetHitTester = targetHitTester;
    }

    public void setCards(List<Card> cards) {
        setCards(cards, 0);
    }

    public void setCards(List<Card> cards, int animatedCardCount) {
        clearChildren();
        selectedCardActor = null;
        draggingCardActor = null;
        draggingCardReadyToPlay = false;
        selectedHandIndex = -1;

        if (cards == null) {
            return;
        }

        int animationStartIndex = Math.max(0, cards.size() - Math.max(0, animatedCardCount));
        for (int i = 0; i < cards.size(); i++) {
            final int handIndex = i;
            final CardActor cardActor = new CardActor(cards.get(i), skin, assets);
            cardActor.setTouchable(Touchable.enabled);
            cardActor.setOrigin(Constants.CARD_WIDTH / 2f, 0f);
            cardActor.addListener(new InputListener() {
                private float pressStageY;
                private boolean dragged;
                private boolean selectedBeforePress;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer != 0) {
                        return false;
                    }

                    selectedBeforePress = selectedCardActor == cardActor && selectedHandIndex == handIndex;
                    selectCard(handIndex, cardActor);
                    draggingCardActor = cardActor;
                    draggingCardReadyToPlay = false;
                    dragged = false;
                    pressStageY = event.getStageY();
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (pointer != 0) {
                        return;
                    }

                    dragged = true;
                    Vector2 stagePoint = new Vector2(event.getStageX(), event.getStageY());
                    cardActor.getParent().stageToLocalCoordinates(stagePoint);
                    cardActor.setPosition(stagePoint.x - cardActor.getWidth() / 2f, stagePoint.y - cardActor.getHeight() / 2f);
                    cardActor.setRotation(0f);
                    draggingCardReadyToPlay = isDraggedFarEnough(pressStageY, event.getStageY());
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer != 0) {
                        return;
                    }

                    if (cardActor.getCard().requiresTarget()) {
                        if (targetHitTester != null && targetHitTester.isTargetHit(event.getStageX(), event.getStageY())) {
                            draggingCardActor = null;
                            draggingCardReadyToPlay = false;
                            if (playRequestHandler != null && playRequestHandler.playSelectedCard()) {
                                return;
                            }
                        }
                        else {
                            if (selectedBeforePress && !dragged) {
                                clearSelection();
                            }
                        }

                        draggingCardActor = null;
                        draggingCardReadyToPlay = false;
                        layoutHandCards();
                        return;
                    }

                    if (dragged && isDraggedFarEnough(pressStageY, event.getStageY())) {
                        draggingCardActor = null;
                        draggingCardReadyToPlay = false;
                        if (playRequestHandler != null && playRequestHandler.playSelectedCard()) {
                            return;
                        }

                        layoutHandCards();
                        return;
                    }

                    if (selectedBeforePress && !dragged) {
                        clearSelection();
                    }

                    draggingCardActor = null;
                    draggingCardReadyToPlay = false;
                    layoutHandCards();
                }
            });

            if (i >= animationStartIndex) {
                float zoneWidth = getWidth() > 0f ? getWidth() : Gdx.graphics.getWidth();
                float visibleCardCount = cards.size();
                float middleIndex = (visibleCardCount - 1f) / 2f;
                float spread = visibleCardCount == 1f ? 0f : Math.min(60f, Math.max(36f, (zoneWidth - Constants.CARD_WIDTH) * 0.18f / middleIndex));
                float layoutIndex = i;
                float offset = layoutIndex - middleIndex;
                float normalizedOffset = middleIndex == 0f ? 0f : offset / middleIndex;
                float targetX = zoneWidth / 2f + offset * spread - Constants.CARD_WIDTH / 2f;
                float targetY = BASE_Y + (1f - Math.abs(normalizedOffset)) * CENTER_RAISE;
                float startX = zoneWidth / 2f - Constants.CARD_WIDTH / 2f;
                float startY = BASE_Y - DRAW_START_OFFSET_Y;

                cardActor.setDrawAnimationActive(true);
                cardActor.addAction(new CardDrawAction(startX, startY, targetX, targetY, DRAW_DURATION));
            }

            addActor(cardActor);
        }

        layoutHandCards();
    }

    public int getSelectedHandIndex() {
        return selectedHandIndex;
    }

    public boolean isSelectedCardRequiresTarget() {
        return selectedCardActor != null && selectedCardActor.getCard().requiresTarget();
    }

    public void clearSelection() {
        if (selectedCardActor != null) {
            selectedCardActor.setSelected(false);
        }

        selectedCardActor = null;
        draggingCardActor = null;
        draggingCardReadyToPlay = false;
        selectedHandIndex = -1;
        layoutHandCards();
    }

    public void layoutHandCards() {
        int cardCount = getChildren().size;
        float zoneWidth = getWidth();

        if (cardCount == 0 || zoneWidth <= 0f) {
            return;
        }

        boolean compressHand = draggingCardReadyToPlay && draggingCardActor != null;
        int visibleCardCount = compressHand ? cardCount - 1 : cardCount;
        float middleIndex = (visibleCardCount - 1) / 2f;
        float spread = visibleCardCount == 1 ? 0f : Math.min(60f, Math.max(36f, (zoneWidth - Constants.CARD_WIDTH) * 0.18f / middleIndex));
        float rotation = compressHand ? 2f : MAX_ROTATION;

        for (int i = 0; i < cardCount; i++) {
            CardActor cardActor = (CardActor) getChildren().get(i);
            if (cardActor == draggingCardActor || cardActor.isDrawAnimationActive()) {
                continue;
            }

            float layoutIndex = compressHand ? (i > selectedHandIndex ? i - 1 : i) : i;
            float offset = layoutIndex - middleIndex;
            float normalizedOffset = middleIndex == 0f ? 0f : offset / middleIndex;
            float x = zoneWidth / 2f + offset * spread - Constants.CARD_WIDTH / 2f;
            float y = BASE_Y + (1f - Math.abs(normalizedOffset)) * CENTER_RAISE;

            if (cardActor == selectedCardActor) {
                y += SELECTED_RAISE;
            }

            cardActor.setOrigin(Constants.CARD_WIDTH / 2f, 0f);
            cardActor.setPosition(x, y);
            cardActor.setSize(Constants.CARD_WIDTH, Constants.CARD_HEIGHT);
            cardActor.setRotation(-normalizedOffset * rotation);
            cardActor.setSelected(cardActor == selectedCardActor);
        }

        if (selectedCardActor != null && selectedHandIndex >= 0 && selectedHandIndex < cardCount) {
            selectedCardActor.setSelected(true);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        layoutHandCards();
        super.draw(batch, parentAlpha);
    }

    private void selectCard(int handIndex, CardActor cardActor) {
        if (selectedCardActor != null && selectedCardActor != cardActor) {
            selectedCardActor.setSelected(false);
        }

        selectedCardActor = cardActor;
        selectedHandIndex = handIndex;
        cardActor.setSelected(true);
    }

    private boolean isDraggedFarEnough(float pressStageY, float releaseStageY) {
        return releaseStageY - pressStageY > 60f;
    }
}
