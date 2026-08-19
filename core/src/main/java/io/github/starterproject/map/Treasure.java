package io.github.starterproject.map;

public class Treasure extends MapNode {
    public Treasure() {
        super();
    }

    @Override
    public Runnable getOnClick() {
        return null;
    }

    @Override
    public String getMapTexture() {
        return "nodes/campfire.png";
    }

    @Override
    public String getName() {
        return "Campfire";
    }

    @Override
    public MapNodeType getNodeType() {
        return MapNodeType.CAMPFIRE;
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
