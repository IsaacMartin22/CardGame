package io.github.starterproject.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.starterproject.TheGameClass;

public class RunInfoOverlay extends Table {
    public RunInfoOverlay(final TheGameClass game) {
        super(game.skin);

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
        Label healthText = new Label(game.runInfo.currentHealth + "/" + game.runInfo.maxHealth, game.skin);
        healthText.setFontScale(3f);

        Image goldPng = new Image(game.assets.get("icons/gold.png", Texture.class));
        Label goldText = new Label(String.valueOf(game.runInfo.gold), game.skin);
        goldText.setFontScale(3f);

        this.add(healthPng).width(60).height(60).pad(10);
        this.add(healthText).width(200).height(60).pad(10);
        this.add(goldPng).width(60).height(60).pad(10);
        this.add(goldText).width(60).height(60).pad(10);
    }
}
