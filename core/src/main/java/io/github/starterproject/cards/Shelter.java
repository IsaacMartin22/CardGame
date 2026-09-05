package io.github.starterproject.cards;

public class Shelter extends Card {

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
        return "Shelter";
    }

    @Override
    public String getDescription() {
        return "Gain 8 Block.";
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
        return "cards/shelter.png";
    }
}
