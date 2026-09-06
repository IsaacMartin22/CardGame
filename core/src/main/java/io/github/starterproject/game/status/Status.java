package io.github.starterproject.game.status;

public abstract class Status {
    protected String name;
    protected String description;
    protected int quantity;
    protected String texturePath;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
