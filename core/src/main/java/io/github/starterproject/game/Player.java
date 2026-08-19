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
    public int gold = 100;
}
