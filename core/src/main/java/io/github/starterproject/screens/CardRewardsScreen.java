package io.github.starterproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.starterproject.actors.CardActor;
import io.github.starterproject.cards.Card;
import io.github.starterproject.cards.Defend;
import io.github.starterproject.cards.Strike;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.overlays.RunInfoOverlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CardRewardsScreen implements Screen {
    private final TheGameClass game;
    private final Stage stage;
    private final Screen backgroundScreen;
    private final Texture transparentTexture;
    private final RunInfoOverlay runInfoOverlay;
    private boolean picked;

    public CardRewardsScreen(final TheGameClass game) {
        this.game = game;
        this.backgroundScreen = game.screenStack.peek();
        this.stage = new Stage(new ScreenViewport());
        this.runInfoOverlay = new RunInfoOverlay(game);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        this.transparentTexture = new Texture(pixmap);
        pixmap.dispose();

        Image background = new Image(transparentTexture);
        background.setFillParent(true);
        background.setTouchable(Touchable.enabled);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.defaults().pad(18f);

        Table cardRow = new Table();
        cardRow.defaults().pad(18f);

        for (Card card : createCardChoices()) {
            final CardActor cardActor = new CardActor(card, game.skin, game.assets);
            cardActor.setTouchable(Touchable.enabled);
            cardActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (picked) {
                        return;
                    }
                    picked = true;
                    game.deck.addCardToDeck(card);
                    game.screenStack.pop();
                    dispose();
                }
            });
            cardRow.add(cardActor).width(cardActor.getWidth()).height(cardActor.getHeight());
        }

        root.add(cardRow);
        stage.addActor(background);
        stage.addActor(runInfoOverlay);
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        if (backgroundScreen != null) {
            backgroundScreen.render(delta);
        }

        ScreenUtils.clear(0f, 0f, 0f, 0f);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.screenStack.push(new SettingsScreen(game));
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
        transparentTexture.dispose();
    }

    private List<Card> createCardChoices() {
        List<Supplier<Card>> pool = new ArrayList<>();
        pool.add(Strike::new);
        pool.add(Defend::new);

        List<Card> choices = new ArrayList<>();
        Collections.shuffle(pool);
        for (int i = 0; i < 3; i++) {
            choices.add(pool.get(i % pool.size()).get());
        }
        Collections.shuffle(choices);
        return choices;
    }
}
