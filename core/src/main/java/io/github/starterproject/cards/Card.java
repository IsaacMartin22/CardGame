package io.github.starterproject.cards;

public abstract class Card {
    public int WIDTH = 150;
    public int HEIGHT = 300;

    public abstract int getCost();
    public abstract String getName();
    public abstract String getDescription();
    public abstract CardType getType();
    public abstract Rarity getRarity();
    public abstract boolean isUpgraded();

    public boolean requiresTarget() {
        return getType() == CardType.ATTACK;
    }

    public abstract String getArtworkPath();
}
