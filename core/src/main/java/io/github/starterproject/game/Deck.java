package io.github.starterproject.game;

import io.github.starterproject.cards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();
//        for (int i = 0; i < 5; i++) {
//            addCard(new Defend());
//            addCard(new Strike());
//        }
        addCard(new Sunder());
        addCard(new Debilitate());
        addCard(new Skewer());
        addCard(new Shelter());
        addCard(new Search());
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void addCardToDeck(Card card) {
        addCard(card);
    }
}
