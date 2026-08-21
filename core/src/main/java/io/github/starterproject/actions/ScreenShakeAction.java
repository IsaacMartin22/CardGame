package io.github.starterproject.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ScreenShakeAction extends TemporalAction {
    private final float intensity;
    private float baseX;
    private float baseY;

    public ScreenShakeAction(float duration, float intensity) {
        this.intensity = intensity;
        setDuration(duration);
    }

    @Override
    protected void begin() {
        Actor target = getTarget();
        baseX = target.getX();
        baseY = target.getY();
    }

    @Override
    protected void update(float percent) {
        Actor target = getTarget();
        float falloff = 1f - Interpolation.fade.apply(percent);
        float shakeAmount = intensity * falloff;
        float offsetX = MathUtils.random(-shakeAmount, shakeAmount);
        float offsetY = MathUtils.random(-shakeAmount, shakeAmount);

        target.setPosition(baseX + offsetX, baseY + offsetY);
    }

    @Override
    protected void end() {
        Actor target = getTarget();
        target.setPosition(baseX, baseY);
    }
}
