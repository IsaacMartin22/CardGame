package io.github.starterproject.game;

import io.github.starterproject.cards.Card;
import io.github.starterproject.cards.Defend;
import io.github.starterproject.cards.Search;
import io.github.starterproject.cards.Shelter;
import io.github.starterproject.cards.Skewer;
import io.github.starterproject.cards.Strike;

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
        for (int i = 0; i < 5; i++) {
            addCard(new Defend());
            addCard(new Strike());
        }
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
