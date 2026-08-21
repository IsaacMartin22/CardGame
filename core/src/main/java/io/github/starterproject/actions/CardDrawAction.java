package io.github.starterproject.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import io.github.starterproject.actors.CardActor;

public class CardDrawAction extends TemporalAction {
    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;
    private final float liftHeight;
    private final float startScale;
    private final float endScale;
    private final float startRotation;
    private final float endRotation;

    public CardDrawAction(float startX, float startY, float endX, float endY, float duration) {
        this(startX, startY, endX, endY, duration, 64f, 0.85f, 1f, -10f, 0f);
    }

    public CardDrawAction(float startX, float startY, float endX, float endY, float duration, float liftHeight, float startScale, float endScale, float startRotation, float endRotation) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.liftHeight = liftHeight;
        this.startScale = startScale;
        this.endScale = endScale;
        this.startRotation = startRotation;
        this.endRotation = endRotation;
        setDuration(duration);
    }

    @Override
    protected void begin() {
        Actor actor = getTarget();
        actor.setPosition(startX, startY);
        actor.setScale(startScale);
        actor.setRotation(startRotation);
    }

    @Override
    protected void update(float percent) {
        Actor actor = getTarget();
        float eased = Interpolation.sineOut.apply(percent);
        float arc = (float) Math.sin(Math.PI * eased) * liftHeight;

        actor.setPosition(
            startX + (endX - startX) * eased,
            startY + (endY - startY) * eased + arc
        );
        actor.setScale(startScale + (endScale - startScale) * eased);
        actor.setRotation(startRotation + (endRotation - startRotation) * eased);
    }

    @Override
    protected void end() {
        Actor actor = getTarget();
        actor.setPosition(endX, endY);
        actor.setScale(endScale);
        actor.setRotation(endRotation);

        if (actor instanceof CardActor) {
            ((CardActor) actor).setDrawAnimationActive(false);
        }
    }
}
