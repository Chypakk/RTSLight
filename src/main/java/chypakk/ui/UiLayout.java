package chypakk.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class UiLayout {
    private final Screen screen;

    public UiLayout(Screen screen) {
        this.screen = screen;
    }

    public Rectangle getBounds(UiRegion region) {
        TerminalSize size = screen.getTerminalSize();
        int x = (int) (size.getColumns() * region.getLeft());
        int y = (int) (size.getRows() * region.getTop());
        int w = (int) (size.getColumns() * region.getWidth());
        int h = (int) (size.getRows() * region.getHeight());
        return new Rectangle(x, y, w, h);
    }

    public void clear(TextGraphics graphics, UiRegion region) {
        Rectangle bounds = getBounds(region);
        for (int y = (int) bounds.getY(); y < bounds.getY() + bounds.getHeight(); y++) {
            graphics.putString((int) bounds.getX(), y, " ".repeat((int) bounds.getWidth()));
        }
    }

    public void drawBox(TextGraphics graphics, UiRegion region) {
        Rectangle bounds = getBounds(region);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int w = (int) bounds.getWidth();
        int h = (int) bounds.getHeight();

        if (w < 2 || h < 2) return;

        // Верхняя граница
        graphics.setCharacter(x, y, '┌');
        for (int i = 1; i < w - 1; i++) {
            graphics.setCharacter(x + i, y, '─');
        }
        graphics.setCharacter(x + w - 1, y, '┐');

        // Боковые границы
        for (int i = 1; i < h - 1; i++) {
            graphics.setCharacter(x, y + i, '│');
            graphics.setCharacter(x + w - 1, y + i, '│');
        }

        // Нижняя граница
        graphics.setCharacter(x, y + h - 1, '└');
        for (int i = 1; i < w - 1; i++) {
            graphics.setCharacter(x + i, y + h - 1, '─');
        }
        graphics.setCharacter(x + w - 1, y + h - 1, '┘');
    }

    public <T> void renderItemList(TextGraphics graphics,
                                   UiRegion region,
                                   List<T> items,
                                   Function<T, String> labelProvider,
                                   Function<T, Integer> countProvider,
                                   Function<T, Integer> almostRemovedProvider) throws IOException {
        clear(graphics, region);
        drawBox(graphics, region);

        Rectangle bounds = getBounds(region);
        int startX = (int) (bounds.getX() + 2);
        int startY = (int) (bounds.getY() + 1);
        int maxHeight = (int) (bounds.getHeight() - 2);

        for (int i = 0; i < Math.min(items.size(), maxHeight); i++) {
            T item = items.get(i);
            int y = startY + i;

            String baseText = String.format("%s: %d",
                    labelProvider.apply(item),
                    countProvider.apply(item)
            );

            graphics.putString(startX, y, baseText);

            int almostRemoved = almostRemovedProvider.apply(item);
            if (almostRemoved > 0) {
                graphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
                String suffix = String.format(" (%d)", almostRemoved);
                graphics.putString(startX + baseText.length(), y, suffix);
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
            }
        }

        screen.refresh();
    }

}