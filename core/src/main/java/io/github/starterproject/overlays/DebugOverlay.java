package io.github.starterproject.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DebugOverlay {
    private final Label label;
    private final Table table;
    private boolean debugEnabled = true;

    public DebugOverlay(Stage stage, Skin skin) {
        label = new Label("", skin);
        label.setAlignment(Align.left);

        table = new Table();
        table.bottom().left();
        table.setFillParent(true);

        table.add(label)
            .top()
            .left()
            .pad(10);

        stage.addActor(table);
    }

    public void update(String screenName) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            debugEnabled = !debugEnabled;
            this.setVisible(debugEnabled);
        }

        Runtime runtime = Runtime.getRuntime();

        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long usedMemoryMb = usedMemory / 1024 / 1024;
        int fps = Gdx.graphics.getFramesPerSecond();

        label.setText(
            "FPS: " + fps +
                "\nMemory: " + usedMemoryMb + " MB" +
                "\nScreen: " + screenName
        );
    }

    private void setVisible(boolean visible) {
        table.setVisible(visible);
    }
}
