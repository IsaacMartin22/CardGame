package io.github.starterproject.map;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    public int number;
    private List<MapNode> nodes;

    public Floor() {
        this.number = 1;
        this.nodes = new ArrayList<>();
        generateNodes();
    }

    public void generateNodes() {
        nodes.clear();
        // Add ancient
        // Add normal enemy

        // randomly generate

        // Add Campfire
        // Add boss
    }

    public void increaseFloor() {
        number++;
        generateNodes();
    }
}
