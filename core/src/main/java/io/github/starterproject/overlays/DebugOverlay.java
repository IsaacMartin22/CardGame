package io.github.starterproject.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import io.github.starterproject.game.TheGameClass;

public class DebugOverlay {
    private final Label label;
    private final Table table;
    private final TheGameClass game;
    private boolean debugEnabled = true;

    public DebugOverlay(Stage stage, Skin skin, TheGameClass game) {
        this.game = game;
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
                "\nScreen: " + screenName +
                "\nScreen Stack: " + getScreenStackText()
        );
    }

    private String getScreenStackText() {
        StringBuilder stackText = new StringBuilder();
        for (Screen screen : game.screenStack.getScreens()) {
            if (stackText.length() > 0) {
                stackText.append(" -> ");
            }
            stackText.append(screen.getClass().getSimpleName());
        }
        return stackText.toString();
    }

    private void setVisible(boolean visible) {
        table.setVisible(visible);
    }
}
