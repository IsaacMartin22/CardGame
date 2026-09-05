package io.github.starterproject.screens;

import com.badlogic.gdx.Screen;
import io.github.starterproject.game.TheGameClass;

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

    public Screen peekBelowTop() {
        return screenStack.size() < 2 ? null : screenStack.get(screenStack.size() - 2);
    }

    public int size() {
        return screenStack.size();
    }

    public void clear() {
        screenStack.clear();
    }
}
