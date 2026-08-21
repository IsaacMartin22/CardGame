package io.github.starterproject.cards;

import java.util.Locale;

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

    public String getArtworkPath() {
        String name = getName();
        if (name == null || name.trim().isEmpty()) {
            return getTypeArtworkPath();
        }

        String normalizedName = name.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
        return "cards/" + normalizedName + ".png";
    }

    private String getTypeArtworkPath() {
        switch (getType()) {
            case ATTACK:
                return "cards/bone.png";
            case SKILL:
                return "cards/leather.png";
            case POWER:
                return "cards/porkchop.png";
            default:
                return "cards/bone.png";
        }
    }
}
