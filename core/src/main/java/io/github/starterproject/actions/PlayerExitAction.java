package io.github.starterproject.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import java.util.function.DoubleSupplier;

public class PlayerExitAction extends TemporalAction {
    private final DoubleSupplier worldWidth;
    private final Runnable onComplete;
    private float startX;
    private float startY;

    public PlayerExitAction(DoubleSupplier worldWidth, Runnable onComplete) {
        super(0.9f, Interpolation.pow2In);
        this.worldWidth = worldWidth;
        this.onComplete = onComplete;
    }

    @Override
    protected void begin() {
        startX = getTarget().getX();
        startY = getTarget().getY();
    }

    @Override
    protected void update(float percent) {
        float endX = (float) worldWidth.getAsDouble() + 2f;
        float endY = -getTarget().getHeight() - 1f;
        getTarget().setPosition(
            startX + (endX - startX) * percent,
            startY + (endY - startY) * percent
        );
    }

    @Override
    protected void end() {
        onComplete.run();
    }
}
