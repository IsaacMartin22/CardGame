package io.github.starterproject.map;

public class Elite extends MapNode {
    public EliteEntity entity;

    public enum EliteEntity {
        RAT,
        SLIME,
    }

     public Elite() {
        this.entity = randomEnum(EliteEntity.class);
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
        return MapNodeType.ELITE;
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
