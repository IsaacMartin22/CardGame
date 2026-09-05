package io.github.starterproject.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.screens.DeckScreen;
import io.github.starterproject.screens.SettingsScreen;

public class RunInfoOverlay extends Table {
    private static final float BAR_HEIGHT = 80f;
    private static final float ICON_SIZE = 60f;
    private final TheGameClass game;
    private final com.badlogic.gdx.scenes.scene2d.ui.Label healthText;
    private final com.badlogic.gdx.scenes.scene2d.ui.Label goldText;

    public RunInfoOverlay(final TheGameClass game) {
        super(game.skin);
        this.game = game;

        this.top().left();

        // Create a blue background bar texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        Texture bgTexture = new Texture(pixmap);
        pixmap.dispose();

        this.setBackground(new TextureRegionDrawable(bgTexture));

        // Set the bar to span the full width and have fixed height
        this.setWidth(Gdx.graphics.getWidth());
        this.setHeight(BAR_HEIGHT);
        this.setPosition(0, Gdx.graphics.getHeight() - BAR_HEIGHT);

        com.badlogic.gdx.scenes.scene2d.ui.Image healthPng = new com.badlogic.gdx.scenes.scene2d.ui.Image(game.assets.get("icons/health.png", Texture.class));
        this.healthText = new com.badlogic.gdx.scenes.scene2d.ui.Label(game.player.currentHP + "/" + game.player.maxHP, game.skin);
        this.healthText.setFontScale(3f);

        com.badlogic.gdx.scenes.scene2d.ui.Image goldPng = new com.badlogic.gdx.scenes.scene2d.ui.Image(game.assets.get("icons/gold.png", Texture.class));
        this.goldText = new com.badlogic.gdx.scenes.scene2d.ui.Label(String.valueOf(game.player.gold), game.skin);
        this.goldText.setFontScale(3f);

        this.add(healthPng).width(60).height(60).pad(10);
        this.add(this.healthText).width(200).height(60).pad(10);
        this.add(goldPng).width(60).height(60).pad(10);
        this.add(this.goldText).width(60).height(60).pad(10);

        this.add().expandX();

        TextButton deckButton = new TextButton("Deck", game.skin);
        deckButton.getLabel().setFontScale(1.2f);
        deckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.screenStack.peek() instanceof DeckScreen) {
                    game.screenStack.pop();
                    return;
                }
                game.screenStack.push(new DeckScreen(game));
            }
        });
        this.add(deckButton).width(120).height(ICON_SIZE).padRight(12);

        TextButton settingsButton = new TextButton("Settings", game.skin);
        settingsButton.getLabel().setFontScale(1.2f);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.push(new SettingsScreen(game));
            }
        });
        this.add(settingsButton).width(180).height(ICON_SIZE).padRight(12);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setWidth(Gdx.graphics.getWidth());
        this.setPosition(0, Gdx.graphics.getHeight() - BAR_HEIGHT);
        healthText.setText(game.player.currentHP + "/" + game.player.maxHP);
        goldText.setText(String.valueOf(game.player.gold));
    }
}
