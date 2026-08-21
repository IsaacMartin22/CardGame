package io.github.starterproject.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.starterproject.map.MapNode;

import java.util.function.Consumer;

public class MapNodeActor extends Actor {
    private static Texture whitePixel;

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
        if (node.visited) {
            ensureWhitePixel();
            float x = getX();
            float y = getY();
            float width = getWidth();
            float height = getHeight();
            float thickness = Math.max(3f, Math.min(width, height) * 0.15f);
            float length = Math.max(width, height) * 1.15f;
            float centerX = x + width / 2f;
            float centerY = y + height / 2f;

            batch.setColor(Color.RED);
            batch.draw(whitePixel, centerX - length / 2f, centerY - thickness / 2f, length / 2f, thickness / 2f, length, thickness, 1f, 1f, 45f, 0, 0, 1, 1, false, false);
            batch.draw(whitePixel, centerX - length / 2f, centerY - thickness / 2f, length / 2f, thickness / 2f, length, thickness, 1f, 1f, -45f, 0, 0, 1, 1, false, false);
            batch.setColor(Color.WHITE);
        }
    }

    public static void disposeTemplates() {
        if (whitePixel != null) {
            whitePixel.dispose();
            whitePixel = null;
        }
    }

    private static void ensureWhitePixel() {
        if (whitePixel != null) {
            return;
        }

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }
}
