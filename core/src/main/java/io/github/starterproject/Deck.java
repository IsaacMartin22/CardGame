package io.github.starterproject;

import io.github.starterproject.cards.Card;
import io.github.starterproject.cards.Defend;
import io.github.starterproject.cards.Strike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    public List<Card> cards = new ArrayList<>();

    public Deck() {
        for (int i = 0; i < 5; i++) {
            cards.add(new Defend());
            cards.add(new Strike());
        }

        shuffle();
    }

    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
}
