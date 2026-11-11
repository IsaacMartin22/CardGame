package io.github.starterproject.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class PlayerActor extends Group {
    private static final float IMAGE_SIZE = 400f;
    private static final float LABEL_HEIGHT = 24f;
    private static final float LABEL_SPACING = 4f;
    private static final float PADDING = 6f;

    private final Image textureImage;
    private final Label healthLabel;
    private final Label blockLabel;

    public PlayerActor(Skin skin, Texture texture) {
        this.textureImage = new Image(texture);
        this.healthLabel = new Label("", skin);
        this.blockLabel = new Label("", skin);

        textureImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
        textureImage.setOrigin(IMAGE_SIZE / 2f, IMAGE_SIZE / 2f);
        textureImage.addAction(Actions.forever(Actions.sequence(
            Actions.moveBy(-1.5f, 0f, 0.05f),
            Actions.moveBy(3f, 1f, 0.1f),
            Actions.moveBy(-1.5f, -1f, 0.05f),
            Actions.delay(0.8f)
        )));
        healthLabel.setAlignment(Align.center);
        blockLabel.setAlignment(Align.center);

        addActor(textureImage);
        addActor(healthLabel);
        addActor(blockLabel);

        setSize(IMAGE_SIZE, IMAGE_SIZE + LABEL_HEIGHT * 2f + LABEL_SPACING + PADDING * 2f);
        layoutActor();
    }

    public void setStats(int currentHealth, int maxHealth, int currentBlock) {
        healthLabel.setText("HP: " + currentHealth + "/" + maxHealth);
        blockLabel.setText("Block: " + currentBlock);
    }

    private void layoutActor() {
        float contentWidth = getWidth();
        textureImage.setPosition((contentWidth - textureImage.getWidth()) / 2f, LABEL_HEIGHT * 2f + LABEL_SPACING + PADDING);
        healthLabel.setBounds(0f, LABEL_HEIGHT + PADDING, contentWidth, LABEL_HEIGHT);
        blockLabel.setBounds(0f, PADDING, contentWidth, LABEL_HEIGHT);
    }
}
