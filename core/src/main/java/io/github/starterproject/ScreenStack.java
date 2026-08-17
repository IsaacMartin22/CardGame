package io.github.starterproject;

import com.badlogic.gdx.Screen;
import java.util.Stack;

public class ScreenStack {
    private final TheGameClass game;
    private final Stack<Screen> screenStack;

    public ScreenStack(TheGameClass game) {
        this.game = game;
        this.screenStack = new Stack<>();
    }

    public void push(Screen screen) {
        game.setScreen(screen);
        screenStack.push(screen);
    }

    public Screen pop() {
        if (screenStack.isEmpty()) {
            return null;
        }
        screenStack.pop();
        if (screenStack.isEmpty()) {
            return null;
        }
        Screen previousScreen = screenStack.peek();
        game.setScreen(previousScreen);
        return previousScreen;
    }

    public void popToRoot() {
        if (!screenStack.isEmpty()) {
            Screen rootScreen = screenStack.firstElement();
            screenStack.clear();
            screenStack.push(rootScreen);
            game.setScreen(rootScreen);
        }
    }

    public Screen peek() {
        return screenStack.isEmpty() ? null : screenStack.peek();
    }

    public int size() {
        return screenStack.size();
    }

    public void clear() {
        screenStack.clear();
    }
}

