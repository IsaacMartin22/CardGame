package io.github.starterproject.screens;

import com.badlogic.gdx.Screen;
import io.github.starterproject.game.TheGameClass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public <T extends Screen> T findTopmost(Class<T> screenType) {
        for (int i = screenStack.size() - 1; i >= 0; i--) {
            Screen screen = screenStack.get(i);
            if (screenType.isInstance(screen)) {
                return screenType.cast(screen);
            }
        }
        return null;
    }

    public boolean moveToTop(Screen screen) {
        if (screen == null) {
            return false;
        }
        int screenIndex = screenStack.lastIndexOf(screen);
        if (screenIndex < 0) {
            return false;
        }
        if (screenIndex == screenStack.size() - 1) {
            game.setScreen(screen);
            return true;
        }
        List<Screen> screensAbove = new ArrayList<>(screenStack.subList(screenIndex + 1, screenStack.size()));
        screenStack.subList(screenIndex, screenStack.size()).clear();
        screenStack.addAll(screensAbove);
        screenStack.push(screen);
        game.setScreen(screen);
        return true;
    }

    public int size() {
        return screenStack.size();
    }

    public List<Screen> getScreens() {
        return Collections.unmodifiableList(screenStack);
    }

    public void clear() {
        screenStack.clear();
    }
}
