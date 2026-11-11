package io.github.starterproject.actions;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerExitActionTest {
    public static void main(String[] args) {
        Actor player = new Actor();
        player.setSize(400f, 450f);
        player.setPosition(100f, 300f);
        float[] width = {1000f};
        int[] completions = {0};
        player.addAction(new PlayerExitAction(() -> width[0], () -> completions[0]++));

        player.act(0.225f);
        float firstDistance = player.getX() - 100f;
        assert firstDistance > 0f && player.getY() < 300f;
        assert completions[0] == 0;

        player.act(0.225f);
        assert player.getX() - 100f > firstDistance * 2f;
        assert completions[0] == 0;

        width[0] = 1400f;
        player.act(0.45f);
        assert player.getX() >= width[0];
        assert player.getY() + player.getHeight() < 0f;
        assert completions[0] == 1;
        player.act(1f);
        assert completions[0] == 1;
    }
}
