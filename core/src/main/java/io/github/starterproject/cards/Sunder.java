package io.github.starterproject.cards;

public class Sunder extends Card {
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
        return "Sunder";
    }

    @Override
    public String getDescription() {
        return "Apply 3 Vulnerable.";
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
        return "cards/sunder.png";
    }
}
