package io.github.starterproject.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CardFlickAction extends TemporalAction {
    private static final float LOOK_AHEAD_STEP = 0.015f;
    private static final float START_DEPTH = 560f;
    private static final float END_DEPTH = 0f;
    private static final float DEPTH_ARC = 90f;
    private static final float MIN_EDGE_SCALE = 0.38f;
    private static final float BASE_BEND_PROGRESS = 0.58f;
    private static final float MAX_BEND_ROTATION = 14f;

    public enum Target {
        ENEMY,
        PLAYER
    }

    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;
    private final float arcHeight;
    private final float sideCurve;
    private final float depthBoost;
    private final float tiltAmount;
    private final float startScale;
    private final float endScale;
    private final float startRotation;
    private final float spinDegrees;
    private final float spinDirection;
    private final float bendX;
    private final float bendY;
    private final float bendZ;
    private final float bendProgress;
    private final boolean dipThenRise;
    private final float arcMultiplier;
    private final float depthArcMultiplier;
    private final float yArcSign;
    private final float zArcSign;
    private final float spinMultiplier;
    private final Runnable onComplete;

    public CardFlickAction(float startX, float startY, float endX, float endY, float duration, Target target) {
        this(startX, startY, endX, endY, duration, target, null);
    }

    public CardFlickAction(
        float startX,
        float startY,
        float endX,
        float endY,
        float duration,
        Target target,
        Runnable onComplete
    ) {
        this(
            startX,
            startY,
            endX,
            endY,
            duration,
            100f,
            22f,
            0.35f,
            0.26f,
            1f,
            0.84f,
            0f,
            1080f,
            target == Target.ENEMY ? 1f : -1f,
            onComplete
        );
    }

    public CardFlickAction(
        float startX,
        float startY,
        float endX,
        float endY,
        float duration,
        float arcHeight,
        float sideCurve,
        float depthBoost,
        float tiltAmount,
        float startScale,
        float endScale,
        float startRotation,
        float spinDegrees,
        float spinDirection,
        Runnable onComplete
    ) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.arcHeight = arcHeight;
        this.sideCurve = sideCurve;
        this.depthBoost = depthBoost;
        this.tiltAmount = tiltAmount;
        this.startScale = startScale;
        this.endScale = endScale;
        this.startRotation = startRotation;
        this.spinMultiplier = MathUtils.random(0.9f, 1.12f);
        this.spinDegrees = spinDegrees * spinMultiplier;
        this.spinDirection = spinDirection;
        this.arcMultiplier = MathUtils.random(0.85f, 1.25f);
        this.depthArcMultiplier = MathUtils.random(0.82f, 1.2f);
        this.dipThenRise = MathUtils.randomBoolean(0.5f);
        this.yArcSign = dipThenRise ? 1f : (MathUtils.randomBoolean() ? 1f : -1f);
        this.zArcSign = MathUtils.randomBoolean() ? 1f : -1f;
        this.onComplete = onComplete;
        this.bendProgress = dipThenRise
            ? MathUtils.random(0.34f, 0.54f)
            : MathUtils.clamp(BASE_BEND_PROGRESS + MathUtils.random(-0.12f, 0.1f), 0.42f, 0.76f);
        float dx = endX - startX;
        float dy = endY - startY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float safeDistance = distance < 0.0001f ? 1f : distance;
        float perpX = -dy / safeDistance;
        float perpY = dx / safeDistance;
        float bendOffset = (Math.max(120f, distance * 0.3f) + sideCurve * 2f) * MathUtils.random(0.84f, 1.22f);
        float verticalBendOffset = arcHeight * MathUtils.random(0.16f, 0.42f) * yArcSign;
        float depthBase = lerp(START_DEPTH, END_DEPTH, 0.42f);
        float depthBendOffset = START_DEPTH * MathUtils.random(0.1f, 0.32f) * zArcSign;
        this.bendX = startX + dx * 0.42f + perpX * bendOffset * spinDirection;
        if (dipThenRise) {
            float dipFloor = Math.min(startY, endY) - Math.max(75f, arcHeight * MathUtils.random(0.8f, 1.4f));
            this.bendY = dipFloor + perpY * bendOffset * spinDirection * 0.18f;
        }
        else {
            this.bendY = startY + dy * 0.42f + perpY * bendOffset * spinDirection + verticalBendOffset;
        }
        this.bendZ = depthBase + depthBendOffset;
        setDuration(duration);
    }

    @Override
    protected void begin() {
        Actor actor = getTarget();
        actor.setOrigin(actor.getWidth() / 2f, actor.getHeight() / 2f);
        actor.setPosition(startX, startY);
        actor.setScale(startScale);
        actor.setRotation(startRotation);
    }

    @Override
    protected void update(float percent) {
        Actor actor = getTarget();
        float travel = Interpolation.pow2Out.apply(percent);
        float x = sampleX(travel);
        float y = sampleY(travel);
        float z = sampleZ(travel);

        float lookAheadPercent = Math.min(1f, percent + LOOK_AHEAD_STEP);
        float travelAhead = Interpolation.pow2Out.apply(lookAheadPercent);
        float dx = sampleX(travelAhead) - x;
        float dy = sampleY(travelAhead) - y;
        float dz = sampleZ(travelAhead) - z;
        if (Math.abs(dx) < 0.0001f && Math.abs(dy) < 0.0001f) {
            dx = endX - startX;
            dy = endY - startY;
            dz = END_DEPTH - START_DEPTH;
        }

        float pathAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
        float baseRotation = pathAngle + 90f;
        float directionMagnitude = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        float forwardness = directionMagnitude <= 0f ? 0f : Math.abs(dz) / directionMagnitude;
        float edgeScale = 1f - (1f - MIN_EDGE_SCALE) * forwardness;

        float lookBackPercent = Math.max(0f, percent - LOOK_AHEAD_STEP);
        float travelBack = Interpolation.pow2Out.apply(lookBackPercent);
        float backDx = x - sampleX(travelBack);
        float backDy = y - sampleY(travelBack);
        float previousPathAngle = (float) Math.toDegrees(Math.atan2(backDy, backDx));
        float turnDelta = shortestAngleDegrees(previousPathAngle, pathAngle);
        float bendFactor = MathUtils.clamp(turnDelta / 35f, -1f, 1f);

        float baseScale = startScale + (endScale - startScale) * travel;
        float arc = (float) Math.sin(Math.PI * travel);
        float perspectiveScale = baseScale + depthBoost * arc * arcMultiplier;
        float scaleX = perspectiveScale * (1f + tiltAmount * arc * 0.18f);
        float scaleY = perspectiveScale * edgeScale * (1f - tiltAmount * arc * 0.15f);

        float speedFactor = MathUtils.clamp(directionMagnitude / 32f, 0.4f, 1.6f);
        float stretch = arc * (0.06f + 0.1f * speedFactor);
        scaleX *= 1f + stretch * (1f - forwardness * 0.35f);
        scaleY *= 1f - stretch * 0.72f;

        float bendStretch = Math.abs(bendFactor) * arc * 0.1f;
        scaleX *= 1f + bendStretch;
        scaleY *= 1f - bendStretch * 0.6f;

        float wobble = (float) Math.sin(Math.PI * travel * 2f) * 8f * (1f - travel);
        float spin = startRotation + spinDegrees * spinDirection * travel + wobble;
        float bendRotation = bendFactor * arc * MAX_BEND_ROTATION;
        float rotation = baseRotation + spin + bendRotation;

        actor.setPosition(x, y);
        actor.setScale(scaleX, scaleY);
        actor.setRotation(rotation);
    }

    @Override
    protected void end() {
        Actor actor = getTarget();
        if (onComplete != null) {
            onComplete.run();
        }
        actor.remove();
    }

    private float sampleX(float travel) {
        if (travel <= bendProgress) {
            float firstLeg = Interpolation.sine.apply(travel / bendProgress);
            return lerp(startX, bendX, firstLeg);
        }

        float secondLeg = Interpolation.pow2Out.apply((travel - bendProgress) / (1f - bendProgress));
        return lerp(bendX, endX, secondLeg);
    }

    private float sampleY(float travel) {
        float arc = (float) Math.sin(Math.PI * travel) * arcMultiplier * yArcSign;
        if (dipThenRise) {
            if (travel <= bendProgress) {
                float firstLeg = Interpolation.sine.apply(travel / bendProgress);
                float dipArc = (float) Math.sin(Math.PI * firstLeg);
                return lerp(startY, bendY, firstLeg) - arcHeight * dipArc * 0.18f;
            }

            float secondLeg = Interpolation.pow2Out.apply((travel - bendProgress) / (1f - bendProgress));
            float riseArc = (float) Math.sin(Math.PI * secondLeg);
            return lerp(bendY, endY, secondLeg) + arcHeight * riseArc * Math.max(0.35f, arcMultiplier * 0.7f);
        }

        if (travel <= bendProgress) {
            float firstLeg = Interpolation.sine.apply(travel / bendProgress);
            return lerp(startY, bendY, firstLeg) + arcHeight * arc;
        }

        float secondLeg = Interpolation.pow2Out.apply((travel - bendProgress) / (1f - bendProgress));
        return lerp(bendY, endY, secondLeg) + arcHeight * arc * 0.4f;
    }

    private float sampleZ(float travel) {
        float arc = (float) Math.sin(Math.PI * travel) * depthArcMultiplier * zArcSign;
        if (travel <= bendProgress) {
            float firstLeg = Interpolation.sine.apply(travel / bendProgress);
            return lerp(START_DEPTH, bendZ, firstLeg) + DEPTH_ARC * arc;
        }

        float secondLeg = Interpolation.pow2Out.apply((travel - bendProgress) / (1f - bendProgress));
        return lerp(bendZ, END_DEPTH, secondLeg) + DEPTH_ARC * arc * 0.5f;
    }

    private float lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }

    private float shortestAngleDegrees(float from, float to) {
        float delta = (to - from + 540f) % 360f - 180f;
        return delta;
    }
}
