package io.github.starterproject.cards;

public class Defend extends Card {

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
        return "Defend";
    }

    @Override
    public String getDescription() {
        return "Gain 5 Block.";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public boolean isUpgraded() {
        return false;
    }
}
