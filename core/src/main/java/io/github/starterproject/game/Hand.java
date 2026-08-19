package io.github.starterproject.game;

import io.github.starterproject.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    public static final int HAND_LIMIT = 5;

    private final List<Card> cards = new ArrayList<>();

    public boolean addCard(Card card) {
        if (card == null || isFull()) {
            return false;
        }

        cards.add(card);
        return true;
    }

    public Card removeCard(int index) {
        if (index < 0 || index >= cards.size()) {
            return null;
        }

        return cards.remove(index);
    }

    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            return null;
        }

        return cards.get(index);
    }

    public int size() {
        return cards.size();
    }

    public boolean isFull() {
        return cards.size() >= HAND_LIMIT;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void clear() {
        cards.clear();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
