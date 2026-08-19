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

    public void generateNodes() {
        nodes.clear();
        nodes.add(new Boss());
        nodes.add(new Campfire());

        // Add normal enemy
        // randomly generate

        nodes.add(new Enemy());
        nodes.add(new Ancient());
    }

    public void increaseFloor() {
        number++;
        generateNodes();
    }
}
