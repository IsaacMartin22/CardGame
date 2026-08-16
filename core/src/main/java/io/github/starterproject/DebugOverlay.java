package io.github.starterproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DebugOverlay {

    private final Label label;
    private final Table table;

    public DebugOverlay(Stage stage, Skin skin) {
        label = new Label("", skin);
        label.setAlignment(Align.left);

        table = new Table();
        table.top().left();
        table.setFillParent(true);

        table.add(label)
            .top()
            .left()
            .pad(10);

        stage.addActor(table);
    }

    public void update(String screenName) {
        Runtime runtime = Runtime.getRuntime();

        long usedMemory =
            runtime.totalMemory() - runtime.freeMemory();

        long usedMemoryMb = usedMemory / 1024 / 1024;

        int fps = Gdx.graphics.getFramesPerSecond();

        //int drawCalls = Gdx.graphics.getFramesPerSecond();

        label.setText(
            "FPS: " + fps +
                "\nMemory: " + usedMemoryMb + " MB" +
                "\nScreen: " + screenName
        );
    }

    public void dispose() {
        // The Stage owns the label/table,
        // so there's nothing to dispose here.
    }
}
