package io.github.starterproject.cards;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CardActor extends Actor {
    private static Texture whitePixel;

    private final Card card;
    private final AssetManager assets;
    private final BitmapFont font;

    public CardActor(Card card, Skin skin) {
        this(card, skin, null);
    }

    public CardActor(Card card, Skin skin, AssetManager assets) {
        this.card = card;
        this.assets = assets;
        this.font = skin.get(Label.LabelStyle.class).font;
        setSize(100, 200);
    }

    @Override
    public void draw(Batch batch, float delta) {
        ensureWhitePixel();

        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        Color previousColor = batch.getColor().cpy();
        batch.setColor(Color.WHITE);

        Color rarityColor = getRarityColor(card.getRarity());
        Color frameColor = getFrameColor(card.getRarity());
        Color paperColor = Color.valueOf("EDE2C8");
        Color artSlotColor = Color.valueOf("40362D");

        drawRect(batch, x, y, width, height, paperColor);
        drawBorder(batch, x, y, width, height, frameColor, 3f);

        float padding = Math.max(6f, width * 0.06f);
        float ribbonHeight = Math.max(18f, height * 0.12f);
        float nameHeight = Math.max(20f, height * 0.12f);
        float costSize = Math.max(20f, width * 0.22f);
        float typeBadgeWidth = Math.max(34f, width * 0.34f);
        float bannerY = y + height - padding - ribbonHeight;
        float nameY = y + padding;
        float artBottom = nameY + nameHeight + 6f;
        float artTop = bannerY - 4f;
        float typeBadgeX = x + width - padding - typeBadgeWidth;

        drawRect(batch, x + padding, bannerY, width - padding * 2f, ribbonHeight, rarityColor);
        drawRect(batch, typeBadgeX, bannerY, typeBadgeWidth, ribbonHeight, getTypeColor(card.getType()));
        drawRect(batch, x + padding, nameY, width - padding * 2f, nameHeight, Color.valueOf("F4E7C5"));
        drawRect(batch, x + padding, artBottom, width - padding * 2f, artTop - artBottom, artSlotColor);
        drawBorder(batch, x + padding, artBottom, width - padding * 2f, artTop - artBottom, Color.valueOf("0B0B0B"), 2f);

        Texture artwork = resolveArtworkTexture();
        if (artwork != null) {
            batch.setColor(Color.WHITE);
            drawTextureFit(batch, artwork, x + padding + 4f, artBottom + 4f, width - padding * 2f - 8f, artTop - artBottom - 8f);
        } else {
            drawPlaceholderArt(batch, x + padding + 4f, artBottom + 4f, width - padding * 2f - 8f, artTop - artBottom - 8f);
        }

        drawRect(batch, x + padding, bannerY, costSize, costSize, Color.WHITE);
        drawBorder(batch, x + padding, bannerY, costSize, costSize, Color.BLACK, 2f);

        batch.setColor(Color.WHITE);
        drawCenteredText(batch, font, String.valueOf(card.getCost()), x + padding, bannerY, costSize, costSize, Color.BLACK);
        drawCenteredText(batch, font, card.getType().name(), typeBadgeX, bannerY, typeBadgeWidth, ribbonHeight, Color.WHITE);
        drawCenteredText(batch, font, card.getName(), x + padding, nameY, width - padding * 2f, nameHeight, Color.BLACK);
        drawCenteredText(batch, font, card.getDescription(), x + padding + 2f, y + padding + nameHeight + 2f, width - padding * 2f - 4f, Math.max(24f, artBottom - (y + padding + nameHeight + 4f)), Color.BLACK);

        batch.setColor(previousColor);
    }

    public static void disposeTemplates() {
        if (whitePixel != null) {
            whitePixel.dispose();
            whitePixel = null;
        }
    }

    private static void ensureWhitePixel() {
        if (whitePixel != null) {
            return;
        }

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    private Texture resolveArtworkTexture() {
        if (assets != null) {
            String artworkPath = card.getArtworkPath();
            if (artworkPath != null && assets.isLoaded(artworkPath, Texture.class)) {
                return assets.get(artworkPath, Texture.class);
            }

            String fallbackPath = getFallbackArtworkPath(card.getType());
            if (fallbackPath != null && assets.isLoaded(fallbackPath, Texture.class)) {
                return assets.get(fallbackPath, Texture.class);
            }
        }

        return null;
    }

    private String getFallbackArtworkPath(CardType type) {
        switch (type) {
            case ATTACK:
                return "cards/bone.png";
            case SKILL:
                return "cards/leather.png";
            case POWER:
                return "cards/porkchop.png";
            default:
                return null;
        }
    }

    private void drawPlaceholderArt(Batch batch, float x, float y, float width, float height) {
        drawRect(batch, x, y, width, height, Color.valueOf("3D332B"));
        drawRect(batch, x + width * 0.08f, y + height * 0.08f, width * 0.84f, height * 0.84f, Color.valueOf("5C4B3F"));
        drawBorder(batch, x, y, width, height, Color.valueOf("1A1410"), 2f);
        batch.setColor(Color.WHITE);
        drawCenteredText(batch, font, "ART", x, y, width, height, Color.WHITE);
    }

    private static void drawTextureFit(Batch batch, Texture texture, float x, float y, float width, float height) {
        float textureRatio = texture.getWidth() / (float) texture.getHeight();
        float targetRatio = width / height;
        float drawWidth = width;
        float drawHeight = height;
        float drawX = x;
        float drawY = y;

        if (textureRatio > targetRatio) {
            drawHeight = width / textureRatio;
            drawY = y + (height - drawHeight) / 2f;
        } else {
            drawWidth = height * textureRatio;
            drawX = x + (width - drawWidth) / 2f;
        }

        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

    private static void drawCenteredText(Batch batch, BitmapFont font, String text, float x, float y, float width, float height, Color color) {
        if (text == null || text.isEmpty()) {
            return;
        }

        GlyphLayout layout = new GlyphLayout(font, text);
        font.setColor(color);
        font.draw(batch, layout, x + (width - layout.width) / 2f, y + (height + layout.height) / 2f);
    }

    private static void drawRect(Batch batch, float x, float y, float width, float height, Color color) {
        batch.setColor(color);
        batch.draw(whitePixel, x, y, width, height);
    }

    private static void drawBorder(Batch batch, float x, float y, float width, float height, Color color, float thickness) {
        drawRect(batch, x, y, width, thickness, color);
        drawRect(batch, x, y + height - thickness, width, thickness, color);
        drawRect(batch, x, y, thickness, height, color);
        drawRect(batch, x + width - thickness, y, thickness, height, color);
    }

    private static Color getRarityColor(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return Color.valueOf("BDB9AD");
            case UNCOMMON:
                return Color.valueOf("5F8D72");
            case RARE:
                return Color.valueOf("7B5AA6");
            default:
                return Color.DARK_GRAY;
        }
    }

    private static Color getFrameColor(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return Color.valueOf("7E6B52");
            case UNCOMMON:
                return Color.valueOf("3E6B54");
            case RARE:
                return Color.valueOf("503C72");
            default:
                return Color.DARK_GRAY;
        }
    }

    private static Color getTypeColor(CardType type) {
        switch (type) {
            case ATTACK:
                return Color.valueOf("9E4B3B");
            case SKILL:
                return Color.valueOf("46708A");
            case POWER:
                return Color.valueOf("8A6A2F");
            default:
                return Color.DARK_GRAY;
        }
    }
}
