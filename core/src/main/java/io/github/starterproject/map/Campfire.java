package io.github.starterproject.map;

public class Campfire extends MapNode {

    @Override
    public String getTexture() {
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
