package io.github.starterproject.map;

public class Ancient extends MapNode {
    public AncientEntity entity;
    public enum AncientEntity {
        WHALE
    }

    public Ancient() {
        this.entity = randomEnum(AncientEntity.class);
    }

    @Override
    public Runnable getOnClick() {
        return null;
    }

    @Override
    public String getMapTexture() {
        return "nodes/elite.png";
    }

    @Override
    public String getName() {
        return entity.name();
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
