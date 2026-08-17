package io.github.starterproject.map;

public class Boss extends MapNode {
    BossEntity entity;
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
    public MapNodeType getNodeType() {
        return MapNodeType.ANCIENT;
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
