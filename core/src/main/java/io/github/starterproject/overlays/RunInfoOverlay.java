package io.github.starterproject.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.starterproject.game.TheGameClass;
import io.github.starterproject.screens.SettingsScreen;

public class RunInfoOverlay extends Table {
    private static final float BAR_HEIGHT = 80f;
    private static final float ICON_SIZE = 60f;
    private final TheGameClass game;
    private final Label healthText;
    private final Label goldText;
    private final TextButton settingsButton;

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

        Image healthPng = new Image(game.assets.get("icons/health.png", Texture.class));
        this.healthText = new Label(game.player.currentHP + "/" + game.player.maxHP, game.skin);
        this.healthText.setFontScale(3f);

        Image goldPng = new Image(game.assets.get("icons/gold.png", Texture.class));
        this.goldText = new Label(String.valueOf(game.player.gold), game.skin);
        this.goldText.setFontScale(3f);

        this.add(healthPng).width(60).height(60).pad(10);
        this.add(this.healthText).width(200).height(60).pad(10);
        this.add(goldPng).width(60).height(60).pad(10);
        this.add(this.goldText).width(60).height(60).pad(10);

        this.add().expandX();

        this.settingsButton = new TextButton("Settings", game.skin);
        this.settingsButton.getLabel().setFontScale(1.2f);
        this.settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenStack.push(new SettingsScreen(game));
            }
        });
        this.add(this.settingsButton).width(180).height(ICON_SIZE).padRight(12);
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
