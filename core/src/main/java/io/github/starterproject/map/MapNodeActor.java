package io.github.starterproject.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Consumer;

public class MapNodeActor extends Actor {
    MapNode node;
    Texture texture;

    public MapNodeActor(MapNode node, Consumer<MapNode> onClick, Skin skin) {
        super();
        this.node = node;
        this.texture = new Texture(node.getMapTexture());

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClick != null) {
                    onClick.accept(node);
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
