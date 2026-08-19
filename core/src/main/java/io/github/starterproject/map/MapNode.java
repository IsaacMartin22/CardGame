package io.github.starterproject.map;

import java.util.concurrent.ThreadLocalRandom;

public abstract class MapNode {
    public boolean visited = false;
    public <E extends Enum<E>> E randomEnum(Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();
        int index = ThreadLocalRandom.current().nextInt(constants.length);
        return constants[index];
    }

    public abstract Runnable getOnClick();
    public abstract String getMapTexture();
    public abstract String getName();
    public abstract MapNodeType getNodeType();
    public abstract int getWeight();
}
