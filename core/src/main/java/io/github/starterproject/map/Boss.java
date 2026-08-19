package io.github.starterproject.map;

public class Boss extends MapNode {
    public BossEntity entity;

    public enum BossEntity {
        STAG,
        INKY_STABBY,
        BOULDER_TROLLS,
        WATERFALL,
        FISH,
        HERMIT_CRAB,
    }

    public Boss() {
        this.entity = randomEnum(BossEntity.class);
    }

    @Override
    public Runnable getOnClick() {
        return null;
    }

    @Override
    public String getMapTexture() {
        return "nodes/boss.png";
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
