package io.github.starterproject.map;

public class Enemy extends MapNode {
    public EnemyEntity entity;
    public enum EnemyEntity {
        RAT,
        SLIME,
    }

    public Enemy() {
        this.entity = randomEnum(EnemyEntity.class);
    }

    @Override
    public String getTexture() {
        return "nodes/enemy.png";
    }

    @Override
    public String getName() {
        return entity.name();
    }

    @Override
    public MapNodeType getNodeType() {
        return MapNodeType.ENEMY;
    }

    @Override
    public int getWeight() {
        return 3;
    }
}
