package io.github.starterproject.map;

public class Merchant extends MapNode {
    public Merchant() {
        super();
    }

    @Override
    public Runnable getOnClick() {
        return null;
    }

    @Override
    public String getMapTexture() {
        return "nodes/merchant.png";
    }

    @Override
    public String getName() {
        return "Merchant";
    }

    @Override
    public MapNodeType getNodeType() {
        return MapNodeType.MERCHANT;
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
