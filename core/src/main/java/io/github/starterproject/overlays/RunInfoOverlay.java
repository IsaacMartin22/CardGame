package io.github.starterproject.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.starterproject.game.TheGameClass;

public class RunInfoOverlay extends Table {
    private final TheGameClass game;
    private final Label healthText;
    private final Label goldText;

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
        this.setWidth(com.badlogic.gdx.Gdx.graphics.getWidth());
        this.setHeight(80);
        this.setPosition(0, com.badlogic.gdx.Gdx.graphics.getHeight() - 80);

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
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        healthText.setText(game.player.currentHP + "/" + game.player.maxHP);
        goldText.setText(String.valueOf(game.player.gold));
    }
}
