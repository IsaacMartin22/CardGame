package io.github.starterproject.game.status;

public class Weak extends Status {
    public Weak(int quantity) {
        this.name = "Weak";
        this.description = "Attacks deal 25% less damage.";
        this.quantity = quantity;
        this.texturePath = "status/weak.png";
    }
}
