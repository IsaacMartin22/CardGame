package io.github.starterproject.game;

import io.github.starterproject.cards.Card;
import io.github.starterproject.cards.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Battle {
    private TheGameClass game;
    private BattleEnemy enemy;
    private final Hand hand;
    private final List<Card> drawPile;
    private final List<Card> discardPile;
    private final List<Card> exhaustPile;

    private Turn turn;

    public Battle(TheGameClass game, BattleEnemy enemy) {
        this.game = game;
        this.enemy = enemy;
        this.hand = new Hand();
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.exhaustPile = new ArrayList<>();
        this.turn = Turn.PLAYER;
    }

    public void initializePiles(Deck deck) {
        hand.clear();
        drawPile.clear();
        discardPile.clear();
        exhaustPile.clear();

        if (deck != null && deck.getCards() != null) {
            drawPile.addAll(deck.getCards());
            Collections.shuffle(drawPile);
        }
    }

    public int drawCards(int amount) {
        int drawn = 0;
        for (int i = 0; i < amount; i++) {
            if (hand.isFull()) {
                break;
            }

            if (drawPile.isEmpty() && !discardPile.isEmpty()) {
                drawPile.addAll(discardPile);
                discardPile.clear();
                Collections.shuffle(drawPile);
            }

            if (drawPile.isEmpty()) {
                break;
            }

            Card card = drawPile.remove(drawPile.size() - 1);
            if (hand.addCard(card)) {
                drawn++;
            }
        }

        return drawn;
    }

    public boolean playCardFromHand(int handIndex) {
        Card card = hand.getCard(handIndex);
        if (card == null) {
            return false;
        }

        boolean played = false;
        switch (card.getType()) {
            case ATTACK:
                enemy.currentHP -= 6;
                played = true;
                break;
            case SKILL:
                game.player.currentBlock += 5;
                played = true;
                break;
            case POWER:
                played = false;
                break;
        }

        if (played) {
            discardCardFromHand(handIndex);
        }

        return played;
    }

    public Card discardCardFromHand(int handIndex) {
        Card card = hand.removeCard(handIndex);
        if (card != null) {
            discardPile.add(card);
        }
        return card;
    }

    public Card exhaustCardFromHand(int handIndex) {
        Card card = hand.removeCard(handIndex);
        if (card != null) {
            exhaustPile.add(card);
        }
        return card;
    }

    public void discardHand() {
        while (!hand.isEmpty()) {
            Card card = hand.removeCard(0);
            if (card != null) {
                discardPile.add(card);
            }
        }
    }

    public void endPlayerTurn() {
        discardHand();
        turn = Turn.ENEMY;

        int incomingDamage = 6;
        int blocked = Math.min(game.player.currentBlock, incomingDamage);
        game.player.currentBlock -= blocked;
        incomingDamage -= blocked;
        game.player.currentHP -= incomingDamage;
        game.player.currentBlock = 0;
        endEnemyTurn();
    }

    public void endEnemyTurn() {
        turn = Turn.PLAYER;
        drawCards(Hand.HAND_LIMIT);
    }

    public Turn getTurn() {
        return turn;
    }

    public Hand getHand() {
        return hand;
    }

    public List<Card> getDrawPile() {
        return Collections.unmodifiableList(drawPile);
    }

    public List<Card> getDiscardPile() {
        return Collections.unmodifiableList(discardPile);
    }

    public List<Card> getExhaustPile() {
        return Collections.unmodifiableList(exhaustPile);
    }

    public BattleResult getBattleResult() {
        if (game.player.currentHP <= 0) {
            return BattleResult.DEFEAT;
        }
        else if (enemy.currentHP <= 0) {
            return BattleResult.VICTORY;
        }
        return BattleResult.ONGOING;
    }

    public int getEnemyHealth() {
        return enemy.currentHP;
    }

    public int getPlayerHealth() {
        return game.player.currentHP;
    }

    public enum BattleResult {
        VICTORY,
        DEFEAT,
        ONGOING
    }

    public enum Turn {
        PLAYER,
        ENEMY
    }
}
