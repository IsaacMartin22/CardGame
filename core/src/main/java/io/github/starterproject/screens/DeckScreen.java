package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.Constants;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.actors.CardActor;
import io.github.starterproject.overlays.DebugOverlay;
import io.github.starterproject.overlays.RunInfoOverlay;

public class DeckScreen implements Screen {
    private final TheGameClass game;     // member
    private final Stage stage;
    final DebugOverlay debugOverlay;
    final RunInfoOverlay runInfoOverlay;

    public DeckScreen(final TheGameClass game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        table.top().left();



        this.debugOverlay = new DebugOverlay(stage, game.skin);
        this.runInfoOverlay = new RunInfoOverlay(game);

        table.padTop(80f);
        int cardsPerRow = Math.max(1, (int) ((Gdx.graphics.getWidth() - 20f) / (Constants.CARD_WIDTH + 20f)));
        for (int i = 0; i < game.deck.getCards().size(); i++) {
            table.add(new CardActor(game.deck.getCards().get(i), game.skin, game.assets)).width(Constants.CARD_WIDTH).height(Constants.CARD_HEIGHT).pad(10);
            if ((i + 1) % cardsPerRow == 0 && i < game.deck.getCards().size() - 1) {
                table.row();
            }
        }

        stage.addActor(runInfoOverlay);

        stage.addActor(table);


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BROWN);

        stage.act(delta);
        debugOverlay.update("Deck Screen");
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.screenStack.pop();
        }
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
    }
}
