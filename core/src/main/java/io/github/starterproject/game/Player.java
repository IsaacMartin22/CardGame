package io.github.starterproject.game;

public class Player {
    public enum Character {
        COPPERCLAD,
        SHOOVY_SHIVVY,
        THRONE_GUY,
        BONE_BOY,
        DEFECTIVE,
    }

    public Character character = Character.COPPERCLAD;
    public int currentHP = 100;
    public int maxHP = 100;
    public int currentBlock = 0;
    public int gold = 100;
    private int velocity = 1;

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        if (velocity < 1) {
            throw new IllegalArgumentException("Velocity must be at least 1");
        }
        this.velocity = velocity;
    }

    public void reset() {
        this.character = Character.COPPERCLAD;
        this.currentHP = 100;
        this.maxHP = 100;
        this.currentBlock = 0;
        this.gold = 100;
        this.velocity = 1;
    }
}
