package io.github.starterproject.cards;

public class Debilitate extends Card {
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
        return "Debilitate";
    }

    @Override
    public String getDescription() {
        return "Apply 3 Weak.";
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
        return "cards/debilitate.png";
    }
}
