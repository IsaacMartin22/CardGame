package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.map.MapNode;
import io.github.starterproject.actors.MapNodeActor;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;

public class MapScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;
    private boolean locked;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;

    public MapScreen(final TheGameClass game, boolean locked) {
        this(game);
        this.locked = locked;
    }

    public MapScreen(final TheGameClass game) {
        this.game = game;
        this.locked = false;
        this.stage = new Stage(new ScreenViewport());

        Image mapBackground = new Image(game.assets.get("backgrounds/map.jpg", Texture.class));
        mapBackground.setPosition(game.stage.getWidth() / 2, game.stage.getHeight() / 2, Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.top().center();

        stage.addActor(mapBackground);

        for (MapNode node : game.level.nodes) {
            table.add(new MapNodeActor(node, this::openNode, game.skin)).width(75).height(75).pad(10);
            table.row();
        }

        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);
        stage.addActor(runInfoOverlay);

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GOLDENROD);

        stage.act(delta);
        debugOverlay.update("Map Screen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
        }

        if (!locked) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                game.screenStack.pop();
            }
        }
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                game.screenStack.push(new DeckScreen(game));
            }
        }
    }

    private void openNode(MapNode node) {
        if (!canOpenNode(node)) {
            return;
        }

        node.visited = true;
        switch (node.getNodeType()) {
            case ENEMY:
            case ELITE:
            case BOSS:
                game.screenStack.push(new BattleScreen(game, node.getNodeType()));
                break;
            case CAMPFIRE:
                game.screenStack.push(new CampfireScreen(game));
                break;
            case ANCIENT:
                game.screenStack.push(new AncientScreen(game));
                break;
            case EVENT:
                game.screenStack.push(new EventScreen(game));
                break;
            case MERCHANT:
                game.screenStack.push(new MerchantScreen(game));
                break;
            case TREASURE:
                game.screenStack.push(new TreasureScreen(game));
                break;
        }
    }

    private boolean canOpenNode(MapNode node) {
        int nodeIndex = game.level.nodes.indexOf(node);
        if (nodeIndex < 0 || node.visited) {
            return false;
        }

        for (int i = nodeIndex + 1; i < game.level.nodes.size(); i++) {
            if (!game.level.nodes.get(i).visited) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
        MapNodeActor.disposeTemplates();
    }
}
