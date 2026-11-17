package io.github.starterproject.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import io.github.starterproject.actions.RamAction;

public class PlayerActor extends Group {
    private static final float IMAGE_SIZE = 400f;
    private static final float LABEL_HEIGHT = 24f;
    private static final float LABEL_SPACING = 4f;
    private static final float PADDING = 6f;

    private final Image textureImage;
    private final Label healthLabel;
    private final Label blockLabel;
    private int velocity = 1;
    private boolean ramming;

    public PlayerActor(Skin skin, Texture texture) {
        this.textureImage = new Image(texture);
        this.healthLabel = new Label("", skin);
        this.blockLabel = new Label("", skin);

        textureImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
        textureImage.setOrigin(IMAGE_SIZE / 2f, IMAGE_SIZE / 2f);
        healthLabel.setAlignment(Align.center);
        blockLabel.setAlignment(Align.center);

        addActor(textureImage);
        addActor(healthLabel);
        addActor(blockLabel);

        setSize(IMAGE_SIZE, IMAGE_SIZE + LABEL_HEIGHT * 2f + LABEL_SPACING + PADDING * 2f);
        layoutActor();
        startIdleAnimation();
    }

    public void ramEnemy(float enemyX, Runnable onImpact, Runnable onComplete) {
        textureImage.clearActions();
        layoutActor();
        ramming = true;
        float distance = enemyX - getX() - textureImage.getWidth() * 0.5f;
        textureImage.addAction(new RamAction(distance, onImpact, () -> {
            ramming = false;
            startIdleAnimation();
            onComplete.run();
        }));
    }

    private void startIdleAnimation() {
        textureImage.addAction(Actions.forever(Actions.sequence(
            Actions.moveBy(-1.5f * velocity * velocity, 0f, 0.05f),
            Actions.moveBy(3f * velocity * velocity, velocity * velocity, 0.1f),
            Actions.moveBy(-1.5f * velocity * velocity, -velocity * velocity, 0.05f),
            Actions.delay(0.8f)
        )));
    }

    public void setStats(int currentHealth, int maxHealth, int currentBlock, int velocity) {
        if (this.velocity != velocity) {
            this.velocity = velocity;
            if (!ramming) {
                textureImage.clearActions();
                layoutActor();
                startIdleAnimation();
            }
        }
        healthLabel.setText("HP: " + currentHealth + "/" + maxHealth);
        blockLabel.setText("Block: " + currentBlock + "   Velocity: " + velocity);
    }

    public void showDamage(int amount) {
        if (amount <= 0) {
            return;
        }

        Label damageLabel = new Label(String.valueOf(amount), healthLabel.getStyle());
        damageLabel.setAlignment(Align.center);
        damageLabel.setFontScale(2f);
        damageLabel.setColor(Color.RED);
        damageLabel.setTouchable(Touchable.disabled);
        damageLabel.pack();
        damageLabel.setPosition((getWidth() - damageLabel.getWidth()) / 2f,
            textureImage.getY() + textureImage.getHeight() * 0.55f);
        addActor(damageLabel);
        damageLabel.addAction(Actions.sequence(
            Actions.parallel(Actions.moveBy(0f, 48f, 0.8f), Actions.fadeOut(0.8f)),
            Actions.removeActor()
        ));
    }

    private void layoutActor() {
        float contentWidth = getWidth();
        textureImage.setPosition((contentWidth - textureImage.getWidth()) / 2f, LABEL_HEIGHT * 2f + LABEL_SPACING + PADDING);
        healthLabel.setBounds(0f, LABEL_HEIGHT + PADDING, contentWidth, LABEL_HEIGHT);
        blockLabel.setBounds(0f, PADDING, contentWidth, LABEL_HEIGHT);
    }
}
