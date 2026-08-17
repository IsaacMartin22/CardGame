package io.github.starterproject.map;

public class Campfire extends MapNode {

    @Override
    public MapNodeType getNodeType() {
        return MapNodeType.CAMPFIRE;
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
