package io.github.starterproject.game.status;

public class Vulnerable extends Status {
    public Vulnerable(int quantity) {
        this.name = "Vulnerable";
        this.description = "Takes 50% more damage from attacks.";
        this.quantity = quantity;
        this.texturePath = "status/vulnerable.png";
    }
}
