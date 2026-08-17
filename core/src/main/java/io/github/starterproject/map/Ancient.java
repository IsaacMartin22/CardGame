package io.github.starterproject.map;

public class Ancient extends MapNode {
    AncientEntity entity;
    public enum AncientEntity {
        WHALE
    }
    
    public Ancient() {
        this.entity = randomEnum(AncientEntity.class);
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
