package io.github.starterproject.map;

import java.util.concurrent.ThreadLocalRandom;

public abstract class MapNode {
    public <E extends Enum<E>> E randomEnum(Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();
        int index = ThreadLocalRandom.current().nextInt(constants.length);
        return constants[index];
    }

    public abstract MapNodeType getNodeType();
    public abstract int getWeight();
}
