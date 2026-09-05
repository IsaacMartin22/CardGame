package io.github.starterproject.cards;

public class Skewer extends Card {

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public CardType getType() {
        return CardType.ATTACK;
    }

    @Override
    public String getName() {
        return "Skewer";
    }

    @Override
    public String getDescription() {
        return "Deal 9 Damage.";
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
        return "cards/skewer.png";
    }
}
