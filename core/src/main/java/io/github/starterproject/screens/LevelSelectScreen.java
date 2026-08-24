package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.map.MapNodeType;
import io.github.starterproject.overlays.RunInfoOverlay;

import java.util.Objects;
import java.util.function.Supplier;

public class LevelSelectScreen implements Screen {
    private static final float WORLD_SIZE = 60f;
    private static final float PLAYER_SIZE = 1f;
    private static final float PLAYER_SPEED = 6f;
    private static final float BOUNDARY_LIMIT = WORLD_SIZE * 0.5f - 1.5f;
    private static final float TRIGGER_SIZE = 2.2f;
    private static final Vector3 PLAYER_START = new Vector3(-WORLD_SIZE * 0.25f, PLAYER_SIZE * 0.7f, 0f);

    private final TheGameClass game;
    private final RunInfoOverlay runInfoOverlay;
    private final Stage hudStage;
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final PerspectiveCamera camera;
    private final Model groundModel;
    private final Model playerModel;
    private final Model triggerModel;
    private final Model visitedMarkerModel;
    private final Model wallModel;
    private final ModelInstance groundInstance;
    private final ModelInstance playerInstance;
    private final ModelInstance[] wallInstances;
    private final Vector3 playerPosition;
    private final Vector3 resumePosition;
    private final TriggerZone[] triggerZones;

    public LevelSelectScreen(final TheGameClass game) {
        this.game = game;
        this.modelBatch = new ModelBatch();
        this.hudStage = new Stage(new ScreenViewport());
        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        this.environment.add(new DirectionalLight().set(0.95f, 0.95f, 0.9f, -1f, -0.8f, -0.2f));

        this.runInfoOverlay = new RunInfoOverlay(game);
        this.hudStage.addActor(runInfoOverlay);

        this.camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.near = 0.1f;
        this.camera.far = 100f;

        ModelBuilder builder = new ModelBuilder();
        this.groundModel = builder.createBox(
            WORLD_SIZE,
            0.2f,
            WORLD_SIZE,
            new Material(ColorAttribute.createDiffuse(new Color(0.22f, 0.35f, 0.22f, 1f))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        this.playerModel = builder.createBox(
            PLAYER_SIZE,
            PLAYER_SIZE * 1.4f,
            PLAYER_SIZE,
            new Material(ColorAttribute.createDiffuse(new Color(0.15f, 0.45f, 0.95f, 1f))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        this.triggerModel = builder.createBox(
            TRIGGER_SIZE,
            TRIGGER_SIZE,
            TRIGGER_SIZE,
            new Material(ColorAttribute.createDiffuse(new Color(0.95f, 0.35f, 0.2f, 1f))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        this.visitedMarkerModel = builder.createBox(
            0.8f,
            0.2f,
            0.8f,
            new Material(ColorAttribute.createDiffuse(new Color(1f, 0.95f, 0.15f, 1f))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        this.wallModel = builder.createBox(
            WORLD_SIZE,
            2f,
            0.5f,
            new Material(ColorAttribute.createDiffuse(new Color(0.55f, 0.42f, 0.2f, 1f))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        this.groundInstance = new ModelInstance(groundModel);
        this.playerInstance = new ModelInstance(playerModel);
        this.wallInstances = createWalls();
        this.playerPosition = new Vector3(PLAYER_START);
        this.resumePosition = new Vector3(PLAYER_START);
        this.triggerZones = createTriggerZones();

        updatePlayerTransform();
        updateCamera();


    }

    public LevelSelectScreen(final TheGameClass game, Supplier<Screen> ignoredNextScreenFactory) {
        this(game);
    }

    @Override
    public void render(float delta) {
        if (game.screenStack.peek() != this) {
            return;
        }

        handleMovement(delta);
        updatePlayerTransform();
        updateCamera();

        TriggerZone hitZone = getHitZone();
        if (hitZone != null) {
            hitZone.markVisited();
            resumePosition.set(hitZone.getExitPosition(playerPosition));
            game.screenStack.push(hitZone.createScreen());
            return;
        }

        Gdx.gl.glClearColor(0.09f, 0.11f, 0.16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        modelBatch.begin(camera);
        modelBatch.render(groundInstance, environment);
        for (TriggerZone triggerZone : triggerZones) {
            modelBatch.render(triggerZone.instance, environment);
            ModelInstance visitedMarkerInstance = triggerZone.getVisitedMarkerInstance();
            if (visitedMarkerInstance != null) {
                modelBatch.render(visitedMarkerInstance, environment);
            }
        }
        for (ModelInstance wallInstance : wallInstances) {
            modelBatch.render(wallInstance, environment);
        }
        modelBatch.render(playerInstance, environment);
        modelBatch.end();

        hudStage.act(delta);
        hudStage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.screenStack.pop();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.screenStack.push(new DeckScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        hudStage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        playerPosition.set(resumePosition);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        hudStage.dispose();
        groundModel.dispose();
        playerModel.dispose();
        triggerModel.dispose();
        visitedMarkerModel.dispose();
        wallModel.dispose();
    }

    private void handleMovement(float delta) {
        float movement = PLAYER_SPEED * delta;
        float nextX = playerPosition.x;
        float nextZ = playerPosition.z;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nextX -= movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nextX += movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            nextZ -= movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            nextZ += movement;
        }

        float clampedX = MathUtils.clamp(nextX, -BOUNDARY_LIMIT, BOUNDARY_LIMIT);
        if (!isBlocked(clampedX, playerPosition.z)) {
            playerPosition.x = clampedX;
        }

        float clampedZ = MathUtils.clamp(nextZ, -BOUNDARY_LIMIT, BOUNDARY_LIMIT);
        if (!isBlocked(playerPosition.x, clampedZ)) {
            playerPosition.z = clampedZ;
        }
    }

    private void updatePlayerTransform() {
        playerInstance.transform.idt().setToTranslation(playerPosition);
    }

    private void updateCamera() {
        camera.position.set(playerPosition.x - 8f, 10f, playerPosition.z + 12f);
        camera.lookAt(playerPosition.x, playerPosition.y, playerPosition.z);
        camera.up.set(Vector3.Y);
        camera.update();
    }

    private TriggerZone getHitZone() {
        for (TriggerZone triggerZone : triggerZones) {
            if (!triggerZone.isVisited() && triggerZone.contains(playerPosition)) {
                return triggerZone;
            }
        }
        return null;
    }

    private boolean isBlocked(float x, float z) {
        for (TriggerZone triggerZone : triggerZones) {
            if (triggerZone.isVisited() && triggerZone.contains(x, z)) {
                return true;
            }
        }
        return false;
    }

    private TriggerZone[] createTriggerZones() {
        return new TriggerZone[] {
            createTriggerZone(
                MapNodeType.ANCIENT,
                new Vector3(-18f, 1.1f, -14f),
                new Color(0.76f, 0.56f, 0.22f, 1f),
                () -> new AncientScreen(game)
            ),
            createTriggerZone(
                MapNodeType.ENEMY,
                new Vector3(-6f, 1.1f, -2f),
                new Color(0.84f, 0.24f, 0.24f, 1f),
                () -> new BattleScreen(game, MapNodeType.ENEMY)
            ),
            createTriggerZone(
                MapNodeType.ELITE,
                new Vector3(6f, 1.1f, -2f),
                new Color(0.74f, 0.2f, 0.86f, 1f),
                () -> new BattleScreen(game, MapNodeType.ELITE)
            ),
            createTriggerZone(
                MapNodeType.TREASURE,
                new Vector3(18f, 1.1f, -14f),
                new Color(0.96f, 0.82f, 0.18f, 1f),
                () -> new TreasureScreen(game)
            ),
            createTriggerZone(
                MapNodeType.MERCHANT,
                new Vector3(-18f, 1.1f, 14f),
                new Color(0.24f, 0.72f, 0.42f, 1f),
                () -> new MerchantScreen(game)
            ),
            createTriggerZone(
                MapNodeType.CAMPFIRE,
                new Vector3(-6f, 1.1f, 14f),
                new Color(0.92f, 0.48f, 0.12f, 1f),
                () -> new CampfireScreen(game)
            ),
            createTriggerZone(
                MapNodeType.EVENT,
                new Vector3(6f, 1.1f, 14f),
                new Color(0.22f, 0.56f, 0.92f, 1f),
                () -> new EventScreen(game)
            ),
            createTriggerZone(
                MapNodeType.BOSS,
                new Vector3(18f, 1.1f, 14f),
                new Color(0.72f, 0.12f, 0.12f, 1f),
                () -> new BattleScreen(game, MapNodeType.BOSS)
            )
        };
    }

    private TriggerZone createTriggerZone(MapNodeType type, Vector3 position, Color color, Supplier<Screen> screenFactory) {
        return new TriggerZone(type, position, color, screenFactory);
    }

    private ModelInstance[] createWalls() {
        ModelInstance leftWall = new ModelInstance(wallModel);
        leftWall.transform.idt().setToTranslation(-WORLD_SIZE * 0.5f, 1f, 0f).rotate(Vector3.Y, 90f);

        ModelInstance rightWall = new ModelInstance(wallModel);
        rightWall.transform.idt().setToTranslation(WORLD_SIZE * 0.5f, 1f, 0f).rotate(Vector3.Y, 90f);

        ModelInstance backWall = new ModelInstance(wallModel);
        backWall.transform.idt().setToTranslation(0f, 1f, -WORLD_SIZE * 0.5f);

        ModelInstance frontWall = new ModelInstance(wallModel);
        frontWall.transform.idt().setToTranslation(0f, 1f, WORLD_SIZE * 0.5f);

        return new ModelInstance[] { leftWall, rightWall, backWall, frontWall };
    }

    private final class TriggerZone {
        private final MapNodeType type;
        private final Vector3 position;
        private final Vector3 size;
        private final ModelInstance instance;
        private final ModelInstance visitedMarkerInstance;
        private final Supplier<Screen> screenFactory;
        private boolean visited;

        private TriggerZone(MapNodeType type, Vector3 position, Color color, Supplier<Screen> screenFactory) {
            this.type = Objects.requireNonNull(type, "type");
            this.position = new Vector3(Objects.requireNonNull(position, "position"));
            this.size = new Vector3(TRIGGER_SIZE, TRIGGER_SIZE, TRIGGER_SIZE);
            this.instance = new ModelInstance(triggerModel);
            this.instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
            this.visitedMarkerInstance = new ModelInstance(visitedMarkerModel);
            this.screenFactory = Objects.requireNonNull(screenFactory, "screenFactory");
            this.instance.transform.idt().setToTranslation(this.position);
            this.visitedMarkerInstance.transform.idt().setToTranslation(this.position.x, this.position.y + 1.4f, this.position.z);
        }

        private boolean contains(Vector3 point) {
            return contains(point.x, point.z);
        }

        private boolean contains(float x, float z) {
            float halfWidth = size.x * 0.5f;
            float halfDepth = size.z * 0.5f;
            float playerHalf = PLAYER_SIZE * 0.5f;

            return Math.abs(x - position.x) <= halfWidth + playerHalf
                && Math.abs(z - position.z) <= halfDepth + playerHalf;
        }

        private boolean isVisited() {
            return visited;
        }

        private void markVisited() {
            this.visited = true;
            this.instance.materials.get(0).set(ColorAttribute.createDiffuse(new Color(0.35f, 0.35f, 0.35f, 1f)));
        }

        private Vector3 getExitPosition(Vector3 entryPoint) {
            Vector3 direction = new Vector3(entryPoint.x - position.x, 0f, entryPoint.z - position.z);
            if (direction.len2() < 0.0001f) {
                direction.set(0f, 0f, -1f);
            }
            else {
                direction.nor();
            }

            float offset = size.x * 0.5f + PLAYER_SIZE * 0.5f + 2.5f;
            return new Vector3(
                position.x + direction.x * offset,
                PLAYER_START.y,
                position.z + direction.z * offset
            );
        }

        private Screen createScreen() {
            return screenFactory.get();
        }

        private ModelInstance getVisitedMarkerInstance() {
            return visited ? visitedMarkerInstance : null;
        }
    }
}
