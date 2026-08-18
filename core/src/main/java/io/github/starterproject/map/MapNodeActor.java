package io.github.starterproject.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MapNodeActor extends Actor {
    MapNode node;
    Texture texture;
    public MapNodeActor(MapNode node, Skin skin) {
        super();
        this.node = node;
        this.texture = new Texture(node.getTexture());
        setSize(100, 100);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
