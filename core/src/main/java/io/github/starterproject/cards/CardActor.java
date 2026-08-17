package io.github.starterproject.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class CardActor extends Actor {
    Card card;
    Label label;
    public CardActor(Card card, Skin skin) {
        super();
        this.card = card;
        this.label = new Label("", skin);
        label.setAlignment(Align.center);
        label.setText(card.getCost() + "\n" + card.getName() + "\n" + card.getDescription() + "\n" + card.getType() + "\n" + card.getRarity());
        setSize(100, 200);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        label.setPosition(getX(), getY());
        label.setSize(getWidth(), getHeight());
        label.draw(batch, delta);
    }
}
