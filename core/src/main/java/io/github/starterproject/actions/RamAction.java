package io.github.starterproject.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class RamAction extends TemporalAction {
    private final float distance;
    private final Runnable onImpact;
    private final Runnable onComplete;
    private float startX;
    private float startY;
    private boolean impacted;

    public RamAction(float distance, Runnable onImpact, Runnable onComplete) {
        this.distance = distance;
        this.onImpact = onImpact;
        this.onComplete = onComplete;
        setDuration(0.7f);
    }

    @Override
    protected void begin() {
        startX = getTarget().getX();
        startY = getTarget().getY();
        impacted = false;
    }

    @Override
    protected void update(float percent) {
        float windup = -Math.signum(distance) * 24f;
        float offset;
        if (percent < 0.2f) {
            offset = windup * Interpolation.sine.apply(percent / 0.2f);
        }
        else if (percent < 0.5f) {
            offset = windup + (distance - windup) * Interpolation.pow3In.apply((percent - 0.2f) / 0.3f);
        }
        else {
            float returnProgress = Interpolation.pow3Out.apply((percent - 0.5f) / 0.5f);
            offset = distance * (1f - returnProgress);
        }
        getTarget().setPosition(startX + offset, startY);
        if (!impacted && percent >= 0.5f) {
            impacted = true;
            onImpact.run();
        }
    }

    @Override
    protected void end() {
        getTarget().setPosition(startX, startY);
        onComplete.run();
    }
}
