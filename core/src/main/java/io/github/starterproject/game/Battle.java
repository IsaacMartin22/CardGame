package io.github.starterproject.game;

public class Battle {
    private Player player;
    private BattleEnemy enemy;

    private Turn turn;

    public Battle(Player player, BattleEnemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turn = Turn.PLAYER;
    }

    public void endPlayerTurn() {
        turn = Turn.ENEMY;
    }

    public void endEnemyTurn() {
        turn = Turn.PLAYER;
    }

    public Turn getTurn() {
        return turn;
    }

    public enum Turn {
        PLAYER,
        ENEMY
    }
}
