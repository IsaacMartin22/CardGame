package io.github.starterproject.map;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public int number;
    public List<MapNode> nodes;

    public Level() {
        this.number = 1;
        this.nodes = new ArrayList<>();
        generateNodes();
    }

    public void reset() {
        number = 1;
        generateNodes();
    }

    public void generateNodes() {
        nodes.clear();
        nodes.add(new Boss());
        nodes.add(new Campfire());
        nodes.add(new Merchant());
        nodes.add(new Enemy());
        nodes.add(new Treasure());
        nodes.add(new Enemy());
        nodes.add(new Enemy());
        nodes.add(new Enemy());
        nodes.add(new Ancient());
    }

    public void increaseFloor() {
        number++;
        generateNodes();
    }
}
