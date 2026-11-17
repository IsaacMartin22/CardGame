package io.github.starterproject.cards;

public class Quickstep extends Card {
    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public CardType getType() {
        return CardType.SKILL;
    }

    @Override
    public String getName() {
        return "Quickstep";
    }

    @Override
    public String getDescription() {
        return "Increase your velocity by 1.";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public boolean isUpgraded() {
        return false;
    }

    @Override
    public String getArtworkPath() {
        return null;
    }
}
