package io.github.starterproject.cards;

public class Search extends Card {

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
        return "Search";
    }

    @Override
    public String getDescription() {
        return "Draw 3 cards.";
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
        return "cards/search.png";
    }
}
