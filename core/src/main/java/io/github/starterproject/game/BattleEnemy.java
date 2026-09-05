package io.github.starterproject.game;

public class BattleEnemy {
    public int currentHP = 30;
    public int maxHP = 30;
    public int currentBlock = 0;
    public int vulnerable = 0;
    public int weak = 0;

    public void applyVulnerable(int amount) {
        vulnerable += amount;
    }

    public void applyWeak(int amount) {
        weak += amount;
    }
}
