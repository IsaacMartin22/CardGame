package io.github.starterproject.map;

public class Boss extends MapNode {
    public BossEntity entity;
    public enum BossEntity {
        CEREMONIAL_BEAST,
        VANTOM,
        KIN,
        WATERFALL_GIANT,
        SOUL_FYSH,
        LAGAVULIN_MATRIARCH,
    }

    public Boss() {
        this.entity = randomEnum(BossEntity.class);
    }

    @Override
    public String getTexture() {
        return "nodes/elite.png";
    }

    @Override
    public String getName() {
        return entity.name();
    }

    @Override
    public MapNodeType getNodeType() {
        return MapNodeType.BOSS;
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
