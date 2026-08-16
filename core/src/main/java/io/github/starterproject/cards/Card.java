package io.github.starterproject.cards;

public abstract class Card {

    public abstract int getCost();
    public abstract String getName();
    public abstract String getDescription();
    public abstract CardType getType();
    public abstract Rarity getRarity();
    public abstract boolean isUpgraded();
}
